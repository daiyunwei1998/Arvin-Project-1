package com.stylish.stylish.service;

import com.stylish.stylish.model.FacebookUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacebookGraphApiService {
    private final RestTemplate restTemplate;
    @Value("${facebook.graph.api.url}")
    private String graphApiUrl;

    @Value("${facebook.graph.api.version}")
    private String graphApiVersion;

    public FacebookGraphApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public FacebookUser getUserProfile(String accessToken) {
        String url = String.format("%s/%s/me?fields=name,email,picture.type(large)&access_token=%s", graphApiUrl, graphApiVersion, accessToken);
        return restTemplate.getForObject(url, FacebookUser.class);
    }
}
