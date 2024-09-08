package com.babelbeats.server.controller;

import com.babelbeats.server.model.Music;
import com.babelbeats.server.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @GetMapping
    public ResponseEntity<List<Music>> getAllMusic() {
        List<Music> allMusic =  musicService.getAllMusic();
        return ResponseEntity.ok(allMusic);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Music> getMusicById(@PathVariable Long id) {
        Music music = musicService.getMusicById(id);
        return ResponseEntity.ok(music);
    }

    @GetMapping("/by-ytb/{ytbId}")
    public ResponseEntity<Music> getMusicByYtb(@PathVariable String ytbId) {
        Music music = musicService.getMusicByYtbId(ytbId);
        return ResponseEntity.ok(music);
    }

    @PostMapping
    public ResponseEntity<Music> createMusic(@RequestBody Music music) {
        Music createdMusic =  musicService.saveMusic(music);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMusic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMusic(@PathVariable Long id) {
        musicService.deleteMusic(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllMusic() {
        musicService.deleteAllMusic();
        return ResponseEntity.noContent().build();
    }
}

