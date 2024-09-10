package com.babelbeats.server.model;

import jakarta.persistence.*;

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
}