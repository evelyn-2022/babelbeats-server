package com.babelbeats.server.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Foreign key linking to AppUser
    private AppUser user;

    @ManyToMany
    @JoinTable(
            name = "playlist_music",  // Join table name
            joinColumns = @JoinColumn(name = "playlist_id"),  // Foreign key in the join table referencing Playlist
            inverseJoinColumns = @JoinColumn(name = "music_id")  // Foreign key in the join table referencing Music
    )
    private Set<Music> musicList = new HashSet<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Set<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(Set<Music> musicList) {
        this.musicList = musicList;
    }

    public void addMusic(Music music) {
        this.musicList.add(music);
        music.getPlaylists().add(this);
    }

    public void removeMusic(Music music) {
        this.musicList.remove(music);
        music.getPlaylists().remove(this);
    }
}
