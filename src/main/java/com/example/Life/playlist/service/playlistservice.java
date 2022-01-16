package com.example.Life.playlist.service;

import com.example.Life.playlist.entity.playlist;
import com.example.Life.playlist.entity.playlist_song;
import com.example.Life.playlist.model.playlistmodel;


import com.example.Life.playlist.model.songoutputmodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface playlistservice
{
    List<playlistmodel> getAllPlayLists();
    List<playlistmodel> getAllPlayListsOfUser(long user_id);
    playlistmodel getPlaylistOfUser(long user_id, long playlist_id);
    List<songoutputmodel> getAllSongsOfPlaylist(long playlist_id);
    playlist save(playlist newPlaylist);
    playlist getPlaylist(long playlist_id);
    playlist_song save(playlist_song pls);
    playlist_song getPlaylistSong(long playlist_song_id, long song_id);
}
