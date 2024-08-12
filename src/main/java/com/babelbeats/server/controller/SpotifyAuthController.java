package com.babelbeats.server.controller;

import com.babelbeats.server.dto.SpotifyTokenResponse;
import com.babelbeats.server.model.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.babelbeats.server.service.AppUserService;

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

    @Autowired
    private AppUserService appUserService;

    private RestTemplate restTemplate = new RestTemplate();
    private final String url = "https://accounts.spotify.com/api/token";

    @GetMapping("/spotify/callback")
    public ResponseEntity<SpotifyTokenResponse> spotifyCallback(@RequestParam String code, @RequestParam long id) {
        HttpHeaders headers = createHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<SpotifyTokenResponse> response = restTemplate.postForEntity(url, request, SpotifyTokenResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            SpotifyTokenResponse tokens = response.getBody();

            AppUser user = appUserService.getAppUserById(id);

            assert tokens != null;
            user.setSpotifyAccessToken(tokens.getAccessToken());
            user.setSpotifyRefreshToken(tokens.getRefreshToken());

            appUserService.updateAppUser(id, user);
            return ResponseEntity.ok(tokens);
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

    @PostMapping("/spotify/refresh-token")
    public ResponseEntity<SpotifyTokenResponse> refreshAccessToken(@RequestParam Long id, @RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        HttpHeaders headers = createHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("refresh_token", refreshToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<SpotifyTokenResponse> response = restTemplate.postForEntity(url, request, SpotifyTokenResponse.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            SpotifyTokenResponse tokens = response.getBody();

            AppUser user = appUserService.getAppUserById(id);

            assert tokens != null;
            user.setSpotifyAccessToken(tokens.getAccessToken());
            user.setSpotifyRefreshToken(refreshToken);
            appUserService.updateAppUser(id, user);

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
