package com.example.Life.song.service;

import com.example.Life.song.entity.favorite_song;
import com.example.Life.song.entity.song;
import com.example.Life.song.model.songmodel;
import com.example.Life.song.model.songoutputmodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface songservice
{
    List<songoutputmodel> getAllFavoriteSong(long user_id);
    List<songoutputmodel> getAllSongs();
    songoutputmodel getSong(long song_id);

    song deleteSong(long song_id);

    List<?> findSongInAlbum(long album_id);
    song save(song newSong);
    song findSong(long song_id);
    favorite_song addNewFavSong(long user_id, long track_id);
    favorite_song deleteAFavSong(long user_id, long track_id);
}
