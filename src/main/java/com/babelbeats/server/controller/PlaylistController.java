package com.babelbeats.server.controller;

import com.babelbeats.server.dto.PlaylistDTO;
import com.babelbeats.server.model.Playlist;
import com.babelbeats.server.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> getAllPlaylists() {
        List<PlaylistDTO> playlists = playlistService.getAllPlaylists();
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO> getPlaylistById(@PathVariable Long id) {
        PlaylistDTO playlist = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(playlist);
    }

    // Create a new playlist
    @PostMapping("/user/{userId}")
    public ResponseEntity<Playlist> createPlaylist(@PathVariable Long userId, @RequestBody Playlist playlist) {
        Playlist createdPlaylist = playlistService.createPlaylist(userId, playlist);
        return ResponseEntity.ok(createdPlaylist);
    }

    // Add music to a playlist
    @PostMapping("/{playlistId}/add-music/{musicId}")
    public ResponseEntity<PlaylistDTO> addMusicToPlaylist(@PathVariable Long playlistId, @PathVariable Long musicId) {
        PlaylistDTO updatedPlaylist = playlistService.addMusicToPlaylist(playlistId, musicId);
        return ResponseEntity.ok(updatedPlaylist);
    }

    // Remove music from a playlist
    @DeleteMapping("/{playlistId}/remove-music/{musicId}")
    public ResponseEntity<PlaylistDTO> removeMusicFromPlaylist(@PathVariable Long playlistId, @PathVariable Long musicId) {
        PlaylistDTO updatedPlaylist = playlistService.removeMusicFromPlaylist(playlistId, musicId);
        return ResponseEntity.ok(updatedPlaylist);
    }

    // Rename a playlist
    @PutMapping("/{playlistId}/name")
    public ResponseEntity<PlaylistDTO> updatePlaylistName(@PathVariable Long playlistId, @RequestParam String newName) {
        PlaylistDTO updatedPlaylistDTO = playlistService.updatePlaylistName(playlistId, newName);
        return ResponseEntity.ok(updatedPlaylistDTO);
    }

    // Delete a playlist
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build(); // 204 No Content response
    }
}
