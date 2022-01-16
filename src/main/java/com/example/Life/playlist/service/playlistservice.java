package com.example.Life.playlist.service;

import com.example.Life.playlist.model.playlistmodel;

import com.example.Life.playlist.model.songmodel;
import com.example.Life.playlist.model.songoutputmodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface playlistservice
{
    List<playlistmodel> getAllPlayListsOfUser(long user_id);
    playlistmodel getPlaylistOfUser(long user_id, long playlist_id);
    List<songoutputmodel> getAllSongsOfPlaylist(long playlist_id);
}
