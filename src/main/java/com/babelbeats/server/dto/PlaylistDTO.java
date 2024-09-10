// PlaylistDTO.java
package com.babelbeats.server.dto;

import java.util.Set;

public class PlaylistDTO {
    private Long playlistId;
    private String playlistName;
    private Long userId;
    private String userName;
    private Set<Long> musicIds;

    // Constructors, Getters, and Setters
    public PlaylistDTO(Long playlistId, String playlistName, Long userId, String userName, Set<Long> musicIds) {
        this.playlistId = playlistId;
        this.playlistName = playlistName;
        this.userId = userId;
        this.userName = userName;
        this.musicIds = musicIds;
    }

    public Long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Long playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<Long> getMusicIds() {
        return musicIds;
    }

    public void setMusicIds(Set<Long> musicIds) {
        this.musicIds = musicIds;
    }
}
