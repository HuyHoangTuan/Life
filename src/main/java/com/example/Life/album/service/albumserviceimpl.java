package com.example.Life.album.service;

import com.example.Life.LifeApplication;
import com.example.Life.album.entity.album;
import com.example.Life.album.entity.favorite_album;
import com.example.Life.album.model.albummodel;

import com.example.Life.album.model.favalbummodel;
import com.example.Life.album.model.songalbummodel;
import com.example.Life.album.model.songmodel;
import com.example.Life.album.repo.albumrepo;
import com.example.Life.album.repo.favorite_albumrepo;
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

    @Autowired
    private favorite_albumrepo favoriteAlbumrepo;

    @Override
    public List<albummodel> getAllAlbums()
    {
        return albumRepo.findAllAlbum();
    }
    @Override
    public albummodel getAlbum(long album_id)
    {
        return albumRepo.findAlbum(album_id).get(0);
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
            String path = LifeApplication.defaultDataDir+ "\\" + "tracks" +"\\"
                    +Long.toString(convertedSong.getTrack_id())+".mp3";
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

    @Override
    public List<albummodel> getAllFavAlbum(long user_id)
    {
        List<favalbummodel> listFavAlbum = favoriteAlbumrepo.findALlFavAlbum(user_id);
        List<albummodel> output = new ArrayList<>();
        for(favalbummodel current : listFavAlbum)
        {
            if(current.getActive() ==false) continue;
            output.add(getAlbum(current.getAlbum_id()));
        }
        return output;
    }

    @Override
    public favorite_album addNewFavAlbum(long user_id, long album_id)
    {
        List<favalbummodel> listFavAlbum = favoriteAlbumrepo.findFavoriteAlbumByUserIdAndAlbumId(user_id, album_id);
        for(favalbummodel current : listFavAlbum)
        {
            favorite_album fa = favoriteAlbumrepo.findById(current.getId());
            fa.setActive(true);
            return favoriteAlbumrepo.save(fa);
        }
        favorite_album fa = new favorite_album();
        fa.setActive(true);
        fa.setAlbum_id(album_id);
        fa.setCreator_id(user_id);
        return favoriteAlbumrepo.save(fa);
    }

    @Override
    public favorite_album deleteFavAlbum(long user_id, long album_id)
    {
        List<favalbummodel> listFavAlbum = favoriteAlbumrepo.findFavoriteAlbumByUserIdAndAlbumId(user_id, album_id);
        for(favalbummodel current : listFavAlbum)
        {
            favorite_album fa = favoriteAlbumrepo.findById(current.getId());
            fa.setActive(false);
            return favoriteAlbumrepo.save(fa);
        }
        return null;
    }
}
