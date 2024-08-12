package com.stylish.stylish.security;

import com.stylish.stylish.exception.InvalidRequestBodyException;
import com.stylish.stylish.exception.UserNotFoundException;
import com.stylish.stylish.model.*;
import com.stylish.stylish.repository.UserDAO;

import com.stylish.stylish.utlis.PasswordGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.stylish.stylish.service.FacebookGraphApiService;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserDAO userDAO;
    private final JwtService jwtService;
    @Value("${jwt.access.expired}")
    private int accessExpired;
    private final AuthenticationManager authenticationManager;
    private final FacebookGraphApiService facebookGraphApiService;
    private final PasswordGenerator passwordGenerator;

    public AuthenticationService(PasswordEncoder passwordEncoder, UserDAO userDAO, JwtService jwtService, AuthenticationManager authenticationManager, FacebookGraphApiService facebookGraphApiService, PasswordGenerator passwordGenerator) {
        this.passwordEncoder = passwordEncoder;
        this.userDAO = userDAO;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.facebookGraphApiService = facebookGraphApiService;
        this.passwordGenerator = passwordGenerator;
    }

    public SignUpResponse registerUser(SignUpRequest signUpRequest) throws DataAccessException {
        signUpRequest.validateNative();
        User user = User.builder()
                        .name(signUpRequest.getName())
                        .password(passwordEncoder.encode(signUpRequest.getPassword()))
                        .email(signUpRequest.getEmail()).provider("native")
                        .build();

        userDAO.addUser(user);
        return SignUpResponse.builder()
                .accessToken(jwtService.createLoginAccessToken(user))
                .accessExpired(accessExpired)
                .user(user)
                .build();
    }

    public AuthenticationResponse authenticate(SignInRequest signInRequest) {
        switch (signInRequest.getProvider()) {
            case "native":
                if (signInRequest.getEmail() != null && !signInRequest.getEmail().isBlank() &&
                        signInRequest.getPassword() != null && !signInRequest.getPassword().isBlank()) {
                    return authenticateByNative(signInRequest);
                } else {
                    throw new InvalidRequestBodyException("Missing fields in request body");
                }

	    case "facebook":
                if (signInRequest.getProvider().isBlank()) { throw new InvalidRequestBodyException("Missing fields in request body");}
                return authenticateByFB((signInRequest));
            default:
                throw new AccessDeniedException("Unsupported provider: " + signInRequest.getProvider());
        }
    }

    private AuthenticationResponse authenticateByNative(SignInRequest signInRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(), signInRequest.getPassword()
                )
        );
        User user = userDAO.getUserByEmail(signInRequest.getEmail()).orElseThrow(() -> {return new UserNotFoundException(signInRequest.getEmail());});
              
	String accessToken = jwtService.createLoginAccessToken(user);
	System.out.println(accessToken);      
	return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .accessExpired(accessExpired)
                .user(user)
                .build();
    }

    private AuthenticationResponse authenticateByFB(SignInRequest signInRequest) {
        String fbAccessToken = signInRequest.getAccessToken();

        FacebookUser facebookUser = facebookGraphApiService.getUserProfile(fbAccessToken);

        // query user in database
        Optional<User> userInDb  = userDAO.getUserByEmail(facebookUser.getEmail());
        User user;
        if (userInDb.isPresent()) {
            // already registered
            user = userInDb.get();
        } else {
            // register in our database
            user = User.builder()
                    .name(facebookUser.getName())
                    .email(facebookUser.getEmail())
                    .provider("facebook")
                    .picture(facebookUser.getPicture().getData().getUrl())  // get url inside nested classes
                    .password(passwordEncoder.encode(passwordGenerator.generatePassword(12, true, true, true, true)))
                    .build();
            userDAO.addUser(user);
        }

        return AuthenticationResponse.builder()
                .accessExpired(accessExpired)
                .accessToken(jwtService.createLoginAccessToken(user))
                .user(user)
                .build();
    }
}
