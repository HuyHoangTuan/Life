package com.example.Life.album.service;

import com.example.Life.album.entity.album;
import com.example.Life.album.model.albummodel;
import com.example.Life.album.model.newalbummodel;
import com.example.Life.album.model.songmodel;
import com.example.Life.album.repo.albumrepo;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class albumserviceimpl implements albumservice
{
    private final String defaultDir = "F:\\Life\\Back-End\\src\\main\\java\\com\\example\\Life\\data\\static" ;

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

    @Getter
    @Setter
    class songoutput
    {
        String artist_name;
        Date release_date;
        String track_name;
        long track_id;
        long artist_id;
        long track_num;
        double duration;
    }
    @Override
    public List<?> getSongIn(long album_id){
        List<songmodel> allSong = albumRepo.findSongIn(album_id);
        List<songoutput> convertedAllSong = new ArrayList<>();
        for(songmodel song: allSong)
        {
            songoutput convertedSong = new songoutput();
            convertedSong.artist_name = song.getArtist_name();
            convertedSong.track_name = song.getTrack_name();
            convertedSong.release_date = song.getRelease_date();
            convertedSong.track_id = song.getTrack_id();
            convertedSong.track_num = song.getTrack_num();
            convertedSong.artist_id = song.getArtist_id();
            String path = defaultDir+ "\\" + Long.toString(convertedSong.artist_id)
                    +"\\" + Long.toString(song.getAlbum_id()) +"\\"
                    +Long.toString(convertedSong.track_num)+".mp3";
            File file = new File(path);
            try {
                AudioFileFormat audioFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
                Map properties = audioFileFormat.properties();
                convertedSong.duration = (Long) (properties.get("duration")) * 0.000001;
            } catch (Exception e) { }
            convertedAllSong.add(convertedSong);
        }
        return convertedAllSong;
    }

    @Override
    public album save(long artist_id, String title, Date release_date, long type)
    {
        album Album = new album();
        Album.setArtist_id(artist_id);
        Album.setTitle(title);
        Album.setRelease_date(release_date);
        Album.setType(type);
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

    @Override
    public List<?> getAllAlbum()
    {
        return albumRepo.findAllAlbum();
    }
}
