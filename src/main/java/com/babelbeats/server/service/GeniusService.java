package com.babelbeats.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeniusService {
    private static final String GENIUS_API_URL = "https://api.genius.com";
    private static final String BASE_URL = "https://genius.com/";

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
                    GENIUS_API_URL + "/search?q=" + query,
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

    public String getLyrics(int songId) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Step 1: Fetch song details from Genius API
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + geniusApiToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);
            String songUrl = GENIUS_API_URL + "/songs/" + songId;

            ResponseEntity<String> response = restTemplate.exchange(
                    songUrl,
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            String songPageUrl = extractSongUrlFromResponse(response.getBody());

            if (songPageUrl == null) {
                throw new RuntimeException("Unable to retrieve song URL from Genius.");
            }

            // Step 2: Scrape the lyrics from the song's URL
            return scrapeLyricsFromUrl(songPageUrl);

        } catch (Exception e) {
            throw new RuntimeException("Error fetching lyrics: " + e.getMessage(), e);
        }
    }

    /**
     * Extracts the song URL from the Genius API response JSON.
     *
     * @param responseBody The response body containing the song details.
     * @return The URL of the song page on Genius.
     */
    private String extractSongUrlFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);
            return rootNode.path("response").path("song").path("url").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing song URL from response: " + e.getMessage(), e);
        }
    }

    private String scrapeLyricsFromUrl(String songUrl) {
        if (songUrl == null) {
            throw new IllegalArgumentException("You must supply either `song_id` or `song_url`.");
        }

        String path = songUrl.replace(BASE_URL, "");;

        String htmlContent = makeRequest(path);
        if (htmlContent == null) {
            return null;
        }

        Document document = Jsoup.parse(htmlContent);

        // Find divs containing lyrics
        Elements divs = document.select("div.lyrics, div[class^=Lyrics__Container]");
        if (divs.isEmpty()) {
            System.out.println("Couldn't find the lyrics section. Please report this if the song has lyrics.\n"
                    + "Song URL: " + BASE_URL + path);
            return null;
        }

        // Extract the lyrics text
        StringBuilder lyrics = new StringBuilder();

        for (Element div : divs) {
            lyrics.append(extractVisibleTextWithNewlines(div));
        }

        // Remove section headers like [Verse], [Bridge]
        String lyricsText = lyrics.toString();
            lyricsText = removeSectionHeaders(lyricsText);

        return lyricsText.strip().trim();
    }

    private String extractVisibleTextWithNewlines(Element element) {
        element.select("a").unwrap();
        element.select("span").unwrap();

        return element.html();
    }

    // Helper method to remove section headers
    private String removeSectionHeaders(String lyrics) {
        // Remove section headers like [Verse], [Bridge]
        String cleanedLyrics = lyrics.replaceAll("\\[.*?\\]", "");
        // Remove extra line breaks
        return cleanedLyrics.replaceAll("\n{2,}", "\n");
    }

    // Helper method to make an HTTP request to Genius website
    private String makeRequest(String path) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + path).toUriString();
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
