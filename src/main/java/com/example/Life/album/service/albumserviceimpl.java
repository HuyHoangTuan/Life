package com.example.Life.album.service;

import com.example.Life.LifeApplication;
import com.example.Life.album.entity.album;
import com.example.Life.album.model.albummodel;

import com.example.Life.album.model.songalbummodel;
import com.example.Life.album.model.songmodel;
import com.example.Life.album.repo.albumrepo;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;

import java.io.File;

import java.sql.Date;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class albumserviceimpl implements albumservice
{

    @Autowired
    private albumrepo albumRepo;

    @Override
    public List<albummodel> getAllAlbums()
    {
        return albumRepo.findAllAlbum();
    }
    @Override
    public albummodel getAlbum(long album_id)
    {
        List<albummodel> listAlbums = getAllAlbums();
        for(albummodel current: listAlbums)
        {
            if(current.getAlbum_id() == album_id) return current;
        }
        return null;
    }
    @Override
    public List<albummodel> getArtistAlbum(long artist_id)
    {
        List<albummodel> listAlbums = albumRepo.findAlbumByArtistId(artist_id);
        return listAlbums;
    }
    @Override
    public List<?> getSongIn(long album_id){
        List<songmodel> allSong = albumRepo.findSongIn(album_id);
        List<songalbummodel> convertedAllSong = new ArrayList<>();
        for(songmodel song: allSong)
        {
            songalbummodel convertedSong = new songalbummodel();
            convertedSong.setArtist_id(song.getArtist_id());
            convertedSong.setArtist_name( song.getArtist_name() );
            convertedSong.setTrack_id(song.getTrack_id());
            convertedSong.setTrack_name(song.getTrack_name());
            convertedSong.setTrack_num(song.getTrack_num());
            convertedSong.setRelease_date(song.getRelease_date());
            String path = LifeApplication.defaultDataDir+ "\\" + Long.toString(convertedSong.getArtist_id())
                    +"\\" + Long.toString(song.getAlbum_id()) +"\\"
                    +Long.toString(convertedSong.getTrack_num())+".mp3";
            File file = new File(path);
            try {
                AudioFileFormat audioFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
                Map properties = audioFileFormat.properties();
                convertedSong.setDuration((Long) (properties.get("duration")) * 0.000001);
            } catch (Exception e) { }
            convertedAllSong.add(convertedSong);
        }
        return convertedAllSong;
    }
    @Override
    public album delete(long album_id)
    {
        album currentAlbum = albumRepo.findById(album_id);
        if(currentAlbum == null) return null;
        currentAlbum.setActive(false);
        albumRepo.save(currentAlbum);
        return currentAlbum;
    }
    @Override
    public album findAlbum(long album_id)
    {
        return albumRepo.findById(album_id);
    }
    @Override
    public album save(album currentAlbum)
    {
        return albumRepo.save(currentAlbum);
    }


}
