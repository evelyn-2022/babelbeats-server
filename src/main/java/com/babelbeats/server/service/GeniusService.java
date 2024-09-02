package com.babelbeats.server.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeniusService {
    private static final String GENIUS_API_URL = "https://api.genius.com/search?q=";
    @Value("${genius.api.token}")
    private String geniusApiToken;

    public String searchSong(String songTitle, String artistName, String albumName, String releaseYear) {
        RestTemplate restTemplate = new RestTemplate();

        // Set the headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + geniusApiToken);

        // Create the search query by dynamically including provided parameters
        StringBuilder queryBuilder = new StringBuilder();

        // Append each parameter to the query if it is not null or empty
        if (songTitle != null && !songTitle.isEmpty()) {
            queryBuilder.append(songTitle).append(" ");
        }
        if (artistName != null && !artistName.isEmpty()) {
            queryBuilder.append(artistName).append(" ");
        }
        if (albumName != null && !albumName.isEmpty()) {
            queryBuilder.append(albumName).append(" ");
        }
        if (releaseYear != null && !releaseYear.isEmpty()) {
            queryBuilder.append(releaseYear).append(" ");
        }

        // Trim the trailing space
        String query = queryBuilder.toString().trim();

        // Create an entity with the headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Make the GET request
            ResponseEntity<String> response = restTemplate.exchange(
                    GENIUS_API_URL + query,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            // Return the response body
            return response.getBody();
        } catch (Exception e) {
            // Handle exceptions appropriately, such as logging errors or returning a user-friendly message
            System.err.println("Error occurred while searching for song: " + e.getMessage());
            return null;
        }
    }
}
