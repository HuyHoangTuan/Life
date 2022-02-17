package com.example.Life.album.service;

import com.example.Life.album.entity.album;
import com.example.Life.album.entity.favorite_album;
import com.example.Life.album.model.albummodel;
import org.springframework.stereotype.Service;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Service
public interface albumservice
{
    List<albummodel> getAllAlbums();
    albummodel getAlbum(long album_id);

    List<albummodel> getArtistAlbum(long artist_id);

    List<?> getSongIn(long album_id);

    album findAlbum(long album_id);
    album save(album currentAlbum);
    album delete(long album_id);

    List<albummodel> getAllFavAlbum(long user_id);
    favorite_album addNewFavAlbum(long user_id, long album_id);
    favorite_album deleteFavAlbum(long user_id, long album_id);
}
