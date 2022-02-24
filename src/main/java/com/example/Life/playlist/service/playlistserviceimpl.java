package com.example.Life.playlist.service;

import com.example.Life.LifeApplication;
import com.example.Life.playlist.entity.playlist;
import com.example.Life.playlist.entity.playlist_song;
import com.example.Life.playlist.model.playlistmodel;
import com.example.Life.playlist.model.songmodel;
import com.example.Life.playlist.model.songoutputmodel;
import com.example.Life.playlist.repo.playlist_songrepo;
import com.example.Life.playlist.repo.playlistrepo;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class playlistserviceimpl implements playlistservice
{
    @Autowired
    private playlistrepo playlistRepo;

    @Autowired
    private playlist_songrepo playlist_songRepo;

    @Override
    public List<playlistmodel> getAllPlayListsOfUser(long user_id)
    {
        return playlistRepo.findAllPlaylistsOfUser(user_id);
    }
    @Override
    public playlistmodel getPlaylistOfUser(long user_id, long playlist_id)
    {
        List<playlistmodel> listPlaylists = getAllPlayListsOfUser(user_id);
        for(playlistmodel current: listPlaylists)
        {
            if(current.getId() == playlist_id) return current;
        }
        return null;
    }

    @Override
    public List<songoutputmodel> getAllSongsOfPlaylist(long playlist_id)
    {
        List<songmodel> list = playlist_songRepo.findAllSongsOfPlaylist(playlist_id);
        List<songoutputmodel> output =  new ArrayList<>();
        for(songmodel current: list)
        {
            songoutputmodel out = new songoutputmodel();
            out.setActive(current.getActive());
            out.setPlaylist_id(current.getPlaylist_id());
            out.setAdded_date(current.getAdded_date());
            out.setAlbum_id(current.getAlbum_id());
            out.setArtist_id(current.getArtist_id());
            out.setArtist_name(current.getArtist_name());
            out.setTrack_name(current.getTrack_name());
            out.setTile(current.getTitle());
            out.setTrack_id(current.getTrack_id());
            String path = LifeApplication.defaultDataDir + "\\" + "tracks" + "\\"
                    + Long.toString(current.getTrack_id())+".mp3";
            File file = new File(path);

            try {
                AudioFileFormat audioFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
                Map properties = audioFileFormat.properties();
                out.setDuration((Long) (properties.get("duration")) * 0.000001);
            } catch (Exception e) { }

            output.add(out);
        }
        return output;
    }

    @Override
    public List<playlistmodel> getAllPlayLists()
    {
        return playlistRepo.findAllPlaylists();
    }

    @Override
    public playlist save(playlist newPlaylist)
    {
        return playlistRepo.save(newPlaylist);
    }

    @Override
    public playlist_song save(playlist_song pls)
    {
        return playlist_songRepo.save(pls);
    }

    @Override
    public playlist getPlaylist(long playlist_id)
    {
        return playlistRepo.findById(playlist_id);
    }

    @Override
    public playlist_song getPlaylistSong(long playlist_song_id,long song_id)
    {
        if (playlist_songRepo.findPlaylistSong(playlist_song_id,song_id).size()==0) return null;
        long id = playlist_songRepo.findPlaylistSong(playlist_song_id,song_id).get(0).getId();
        return playlist_songRepo.findById(id);
    }
}
