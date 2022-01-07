package com.example.Life.album.service;

import com.example.Life.album.entity.album;
import com.example.Life.album.model.albummodel;
import com.example.Life.album.model.newalbummodel;
import com.example.Life.album.repo.albumrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class albumserviceimpl implements albumservice
{
    @Autowired
    private albumrepo albumRepo;

    @Override
    public List<albummodel> getAlbum(long artistId, long albumId)
    {
        List<albummodel> listAlbum = albumRepo.findAlbumById(artistId, albumId);
        return listAlbum;
    }

    @Override
    public List<albummodel> getAlbum(long artistId)
    {
        List<albummodel> listAlbum = albumRepo.findAlbum(artistId);
        return listAlbum;
    }

    @Override
    public List<?> getSongIn(long albumId)
    {
        return albumRepo.findSongIn(albumId);
    }

    @Override
    public album save(newalbummodel newAlbummodel)
    {
        album Album = new album();
        Album.setArtist_id(newAlbummodel.getArtist_id());
        Album.setTitle(newAlbummodel.getTitle());
        Album.setRelease_date(newAlbummodel.getRelease_date());
        Album.setType(newAlbummodel.getType());
        Album.setActive(true);
        return albumRepo.save( Album);
    }

    @Override
    public album delete(long albumId)
    {
        List<album> listAlbum = albumRepo.findById(albumId);
        if(listAlbum.size() ==0) return null;
        album currentAlbum = listAlbum.get(0);
        currentAlbum.setActive(false);
        albumRepo.save(currentAlbum);
        return currentAlbum;
    }
}
