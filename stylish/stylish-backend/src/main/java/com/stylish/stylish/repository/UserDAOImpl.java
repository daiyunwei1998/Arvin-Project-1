package com.stylish.stylish.repository;

import com.stylish.stylish.exception.DuplicateEmailException;
import com.stylish.stylish.model.User;
import com.stylish.stylish.model.Role;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Log4j2
@Repository
public class UserDAOImpl implements UserDAO{
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) throws DuplicateKeyException {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }


    @Override
    public void addUser(User user) throws DataAccessException {
        String sql = "INSERT INTO users (provider, name, email, picture, password) " +
                "VALUES (:provider, :name, :email, :picture, :password)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("provider", user.getProvider())
                .addValue("name", user.getName())
                .addValue("email", user.getEmail())
                .addValue("picture", user.getPicture())
                .addValue("password", user.getPassword());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
            long userId = keyHolder.getKey().longValue();
            user.setId(userId);
            setUserRole(userId, getRoleId(user.getRole()));
        } catch (DuplicateKeyException e) {
            // Handle the case where the email is already present in the database
            throw new DuplicateEmailException("User with email " + user.getEmail() + " already exists.");
        } catch (DataAccessException e) {
            // Handle other database access exceptions if needed
            log.warn(e.getCause());
            throw new DataAccessException("An error occurred while inserting user data.", e) {};
        }
    }

    private void setUserRole(long userId, int roleId) {
        String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (:userId, :roleId)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("roleId",roleId);
            namedParameterJdbcTemplate.update(sql, params); // error should be handled by global exception handelr
    }

    private int getRoleId(Role role) {
        String sql = "SELECT id FROM roles WHERE name = :roleName";
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, Map.of("roleName", role.name()), Integer.class);
        } catch (NullPointerException e) {
            log.error("default role is empty, check registration procedure");
            return  2; //default User
        }
    }


    @Override
    public User getUserById(long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper<>(User.class));
        } catch (EmptyResultDataAccessException e) {
            // Handle case when no user is found with the given id
            return null;
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        String sqlUser = "SELECT * FROM users WHERE email = ?";
        String sqlRole = "SELECT r.name " +
                "FROM users u " +
                "JOIN user_roles ur ON u.id = ur.user_id " +
                "JOIN roles r ON ur.role_id = r.id " +
                "WHERE u.email = ? ";

        try {
            // Retrieve user information
            User user = jdbcTemplate.queryForObject(sqlUser, new Object[]{email}, new BeanPropertyRowMapper<>(User.class));

            if (user != null) {
                try {
                    // Retrieve role for the user
                    String role = jdbcTemplate.queryForObject(sqlRole, new Object[]{email}, String.class);
                    // Set the role for the user
                    user.setRole(Role.valueOf(role));
                } catch (EmptyResultDataAccessException e) {
                    // Handle case when no role is found for the user
                    log.warn("No role found for user with email: {}", email);
                    user.setRole(Role.USER); // Or handle as appropriate
                }
            }
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            // Handle case when no user is found with the given email
            return Optional.empty();
        }
    }

    @Override
    public User getUserByUserName(String username) {
        String sql = "SELECT * FROM users WHERE name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User.UserBuilder userBuilder = User.builder()
                            .id(rs.getLong("id"))
                            .name(rs.getString("name"))
                            .password(rs.getString("password"))
                            .email(rs.getString("email"))
                            .picture(rs.getString("picture"))
                            .provider(rs.getString("provider"));

                    String roleString = rs.getString("role");
                    if (roleString != null) {
                        try {
                            Role role = Role.valueOf(roleString.toUpperCase());
                            userBuilder.role(role);
                        } catch (IllegalArgumentException e) {
                            // Handle invalid role value
                            System.err.println("Invalid role value: " + roleString);
                        }
                    }
                    return userBuilder.build();
                }

            });
        } catch (EmptyResultDataAccessException e) {
            // Handle case when no user is found with the given email
            return null;
        }
    }

    @Deprecated
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

}
