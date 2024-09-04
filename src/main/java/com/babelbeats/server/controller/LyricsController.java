package com.babelbeats.server.controller;

import com.babelbeats.server.service.GeniusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LyricsController {
    @Autowired
    private GeniusService geniusService;

    @GetMapping("/songs")
    public ResponseEntity<String> getSongs(
            @RequestParam String songTitle,
            @RequestParam String artistName,
            @RequestParam(required = false) String albumName,
            @RequestParam(required = false) String releaseYear

    ) {
        String response = geniusService.searchSong(songTitle, artistName, albumName, releaseYear);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint to fetch lyrics of a song by its Genius song ID.
     *
     * @param songId The Genius song ID.
     * @return The lyrics of the song.
     */
    @GetMapping("/songs/{songId}")
    public String getLyrics(@PathVariable int songId) {
        return geniusService.getLyrics(songId);
    }
}
