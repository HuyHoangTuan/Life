package com.example.Life.song.service;

import com.example.Life.song.entity.song;
import com.example.Life.song.model.songmodel;
import com.example.Life.song.model.songoutputmodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface songservice
{
    ///List<?> findSong(String content);

    List<songoutputmodel> getAllSongs();
    songoutputmodel getSong(long song_id);

    song deleteSong(long song_id);

    List<?> findSongInAlbum(long album_id);
    song save(song newSong);
}
