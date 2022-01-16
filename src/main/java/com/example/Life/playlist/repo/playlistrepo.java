package com.example.Life.playlist.repo;

import com.example.Life.playlist.entity.playlist;
import com.example.Life.playlist.model.playlistmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface playlistrepo extends JpaRepository<playlist, UUID>
{
    playlist save(playlist p);
    playlist findById(long playlist_id);

    @Query(value = "SELECT pl.active as active, pl.creator_id as creator_id, a.display_name as creator_name, pl.title as title," +
            "pl.id as id " +
            "FROM playlist as pl " +
            "INNER JOIN account as a " +
            "ON a.id = pl.creator_id " +
            "WHERE pl.creator_id = :user_id " +
            "ORDER BY pl.id ",
            nativeQuery = true)
    List<playlistmodel> findAllPlaylistsOfUser(@Param("user_id") long user_id);

    @Query(value = "SELECT pl.active as active, pl.creator_id as creator_id, a.display_name as creator_name, pl.title as title," +
            "pl.id as id " +
            "FROM playlist as pl " +
            "INNER JOIN account as a " +
            "ON a.id = pl.creator_id " +
            "WHERE true = true " +
            "ORDER BY pl.id ",
            nativeQuery = true)
    List<playlistmodel> findAllPlaylists();
}
