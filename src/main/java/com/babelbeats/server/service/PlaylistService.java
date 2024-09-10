package com.babelbeats.server.service;

import com.babelbeats.server.dto.PlaylistDTO;
import com.babelbeats.server.exception.ResourceNotFoundException;
import com.babelbeats.server.model.AppUser;
import com.babelbeats.server.model.Music;
import com.babelbeats.server.model.Playlist;
import com.babelbeats.server.repository.AppUserRepository;
import com.babelbeats.server.repository.MusicRepository;
import com.babelbeats.server.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlaylistService {

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    public List<PlaylistDTO> getAllPlaylists() {
        return playlistRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PlaylistDTO getPlaylistById(long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        return mapToDTO(playlist);
    }

    public Playlist createPlaylist(Long userId, Playlist playlist) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        playlist.setUser(user);
        return playlistRepository.save(playlist);
    }

    public PlaylistDTO addMusicToPlaylist(Long playlistId, Long musicId) {
        // Fetch the playlist by ID
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        // Fetch the music by ID
        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Music not found"));

        // Add the music to the playlist
        playlist.addMusic(music);

        // Save the updated playlist
        Playlist updatedPlaylist = playlistRepository.save(playlist);

        // Use mapToDTO helper method to convert updated Playlist to PlaylistDTO
        return mapToDTO(updatedPlaylist);
    }

    public PlaylistDTO removeMusicFromPlaylist(Long playlistId, Long musicId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new ResourceNotFoundException("Music not found"));

        playlist.removeMusic(music);

        Playlist updatedPlaylist = playlistRepository.save(playlist);

        return mapToDTO(updatedPlaylist);
    }

    public PlaylistDTO updatePlaylistName(Long playlistId, String newName) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        playlist.setName(newName);

        Playlist updatedPlaylist = playlistRepository.save(playlist);

        return mapToDTO(updatedPlaylist);
    }

    public void deletePlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));
        playlistRepository.delete(playlist);
    }

    // Helper method to convert Playlist entity to PlaylistDTO
    private PlaylistDTO mapToDTO(Playlist playlist) {
        Set<Long> musicIds = playlist.getMusicList().stream()
                .map(Music::getId)
                .collect(Collectors.toSet());

        return new PlaylistDTO(
                playlist.getId(),
                playlist.getName(),
                playlist.getUser().getId(),
                playlist.getUser().getName(),
                musicIds
        );
    }
}
