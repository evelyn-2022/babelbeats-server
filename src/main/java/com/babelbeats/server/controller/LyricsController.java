package com.babelbeats.server.controller;

import com.babelbeats.server.service.GeniusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LyricsController {
    @Autowired
    private GeniusService geniusService;

    @GetMapping("/lyrics")
    public ResponseEntity<String> getLyrics(
            @RequestParam String songTitle,
            @RequestParam String artistName,
            @RequestParam(required = false) String albumName,
            @RequestParam(required = false) String releaseYear

    ) {
        String response = geniusService.searchSong(songTitle, artistName, albumName, releaseYear);
        return ResponseEntity.ok(response);
    }
}
