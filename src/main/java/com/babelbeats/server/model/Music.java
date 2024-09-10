package com.babelbeats.server.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(indexes = {
        @Index(name = "idx_ytb_id", columnList = "ytbId")
})
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ytbId;

    @Column(columnDefinition = "TEXT")
    private String lyrics;

    @ManyToMany(mappedBy = "musicList")
    private Set<Playlist> playlists = new HashSet<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getYtbId() {
        return ytbId;
    }

    public void setYtbId(String ytbId) {
        this.ytbId = ytbId;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public Set<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(Set<Playlist> playlists) {
        this.playlists = playlists;
    }
}