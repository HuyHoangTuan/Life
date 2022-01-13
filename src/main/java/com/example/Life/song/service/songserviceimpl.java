package com.example.Life.song.service;

import com.example.Life.LifeApplication;
import com.example.Life.account.repo.accountrepo;
import com.example.Life.song.entity.song;
import com.example.Life.song.model.songmodel;
import com.example.Life.song.model.songoutputmodel;
import com.example.Life.song.repo.songrepo;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import java.io.File;
import java.util.*;

@Service
public class songserviceimpl implements songservice
{



    @Autowired
    private songrepo songRepo;

    @Autowired
    private accountrepo accountRepo;

    @Override
    public List<songoutputmodel> getAllSongs()
    {
        List<songmodel> listSongs = songRepo.findAllSongs();
        List<songoutputmodel> listOutputSongs = new ArrayList<>();
        for(songmodel current: listSongs)
        {
            listOutputSongs.add(getSong(current.getTrack_id()));
        }
        return listOutputSongs;
    }
    @Override
    public songoutputmodel getSong(long song_id)
    {
        List<songmodel> output = songRepo.findSongById(song_id);
        if(output.size() == 0) return null;
        songmodel song = output.get(0);
        songoutputmodel songOutput = new songoutputmodel();
        songOutput.setAlbum_id(song.getAlbum_id());
        songOutput.setArtist_id(song.getArtist_id());
        songOutput.setArtist_name(song.getArtist_name());
        songOutput.setTitle(song.getTitle());
        songOutput.setRelease_date(song.getRelease_date());
        songOutput.setTrack_name(song.getTrack_name());
        songOutput.setTrack_num(song.getTrack_num());
        songOutput.setTrack_id(song.getTrack_id());
        songOutput.setActive(song.getActive());
        String path = LifeApplication.defaultDataDir + "\\" + Long.toString(songOutput.getArtist_id()) + "\\"
                + Long.toString(songOutput.getAlbum_id())+"\\"
                + Long.toString(songOutput.getTrack_num())+".mp3";
        File file = new File(path);
        try {
            AudioFileFormat audioFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
            Map properties = audioFileFormat.properties();
            songOutput.setDuration((Long) (properties.get("duration")) * 0.000001);
        } catch (Exception e) { }
        return songOutput;
    }

    @Override
    public song deleteSong(long song_id)
    {
        song currentSong = songRepo.findById(song_id);
        currentSong.setActive(false);
        return songRepo.save(currentSong);

    }
    @Override
    public List<?> findSongInAlbum(long album_id)
    {
        return songRepo.findSongByAlbum(album_id);
    }

    @Override
    public song save(song newSong)
    {
        return songRepo.save(newSong);
    }

    @Override
    public song findSong(long song_id)
    {
        return songRepo.findById(song_id);
    }
}
