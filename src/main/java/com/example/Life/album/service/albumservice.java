package com.example.Life.album.service;

import com.example.Life.album.entity.album;
import com.example.Life.album.model.albummodel;
import com.example.Life.album.model.newalbummodel;
import org.springframework.stereotype.Service;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Service
public interface albumservice
{
    List<?> getAllAlbum();
    List<albummodel> getAlbum(long artistId);
    List<albummodel> getAlbum(long artistId, long albumId);
    List<?> getSongIn(long albumId) throws IOException, UnsupportedAudioFileException;
    album save(long artist_id, String title, Date release_date, long type);
    album delete(long albumId);
}
