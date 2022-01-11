package com.example.Life.song.service;

import com.example.Life.song.entity.song;
import com.example.Life.song.model.songmodel;
import com.example.Life.song.model.songoutputmodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface songservice
{
    List<?> findSong(String content);
    songoutputmodel getSong(long song_id);
    boolean deleteSong(long songId);
    List<?> findSongInAlbum(long album_id);
    song save(song newSong);
}
