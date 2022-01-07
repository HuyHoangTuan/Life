package com.example.Life.song.service;

import com.example.Life.song.entity.song;
import com.example.Life.song.model.songmodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface songservice
{
    List<?> findSong(String content);
    songmodel getSong(long songId);
    boolean deleteSong(long songId);
    List<?> findSongInAlbum(long album_id);
    song save(song newSong);
}
