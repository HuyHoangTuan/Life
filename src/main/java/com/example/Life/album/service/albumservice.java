package com.example.Life.album.service;

import com.example.Life.album.entity.album;
import com.example.Life.album.model.albummodel;
import com.example.Life.album.model.newalbummodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface albumservice
{
    List<albummodel> getAlbum(long artistId);
    List<albummodel> getAlbum(long artistId, long albumId);
    List<?> getSongIn(long albumId);
    album save(newalbummodel newAlbummodel);
    album delete(long albumId);
}
