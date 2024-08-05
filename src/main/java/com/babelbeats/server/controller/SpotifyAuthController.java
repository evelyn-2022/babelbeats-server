package com.babelbeats.server.controller;

import com.babelbeats.server.dto.SpotifyTokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Base64;
import java.util.Map;

@RestController
public class SpotifyAuthController {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect.uri}")
    private String redirectUri;

    private RestTemplate restTemplate = new RestTemplate();
    private final String url = "https://accounts.spotify.com/api/token";

    @GetMapping("/spotify/callback")
    public ResponseEntity<SpotifyTokenResponse> spotifyCallback(@RequestParam String code) {
        HttpHeaders headers = createHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<SpotifyTokenResponse> response = restTemplate.postForEntity(url, request, SpotifyTokenResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            SpotifyTokenResponse tokens = response.getBody();
            // Save tokens to the database associated with the user

            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    @PostMapping("/spotify/refresh-token")
    public ResponseEntity<SpotifyTokenResponse> refreshAccessToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        HttpHeaders headers = createHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<SpotifyTokenResponse> response = restTemplate.postForEntity(url, request, SpotifyTokenResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            SpotifyTokenResponse tokens = response.getBody();
            // Save the new access token and return it

            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String auth = clientId + ":" + clientSecret;
        String encodedAuth = new String(Base64.getEncoder().encode(auth.getBytes()));
        headers.add("Authorization", "Basic " + encodedAuth);
        return headers;
    }
}
