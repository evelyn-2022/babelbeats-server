package com.babelbeats.server.service;

import com.babelbeats.server.exception.ResourceNotFoundException;
import com.babelbeats.server.model.Music;
import com.babelbeats.server.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicService {
    @Autowired
    private MusicRepository musicRepository;

    public List<Music> getAllMusic() {
        return musicRepository.findAll();
    }

    public Music getMusicById(Long id) {
        return musicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Music not found with id: " + id));
    }

    public Music getMusicByYtbId(String ytbId) {
        return musicRepository.findByYtbId(ytbId)
                .orElseThrow(() -> new ResourceNotFoundException(("Music not found with ytb id: " + ytbId)));
    }

    public Music saveMusic(Music music) {
        return musicRepository.save(music);
    }

    public void deleteMusic(Long id) {
        musicRepository.deleteById(id);
    }

    public void deleteAllMusic() {
        musicRepository.deleteAll();
    }
}
