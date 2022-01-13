package com.example.Life.song.repo;

import com.example.Life.song.entity.song;
import com.example.Life.song.model.songmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface songrepo extends JpaRepository<song, UUID>
{
    @Query(value = "SELECT s.id as track_id , s.track_num as track_num, s.track_name as track_name, " +
            "al.id as album_id, al.title as title, al.type as type, al.release_date as release_date, " +
            "a.id as artist_id, a.display_name as artist_name, a.email as email " +
            "FROM song as s " +
            "INNER JOIN album as al " +
            "ON s.album_id = al.id " +
            "INNER JOIN account as a " +
            "ON al.artist_id = a.id " +
            "WHERE a.role = 1 AND a.active = true AND s.active = true AND al.active = true", nativeQuery = true)
    List<songmodel> findAllSong();

    @Query(value = "SELECT s.id as track_id , s.track_num as track_num, s.track_name as track_name, " +
            "al.id as album_id, al.title as title, al.type as type, al.release_date as release_date, " +
            "a.id as artist_id, a.display_name as artist_name " +
            "FROM song as s " +
            "INNER JOIN album as al " +
            "ON s.album_id = al.id " +
            "INNER JOIN account as a " +
            "ON al.artist_id = a.id " +
            "WHERE a.role = 1 AND a.active = true AND s.active = true AND al.active = true AND s.id = :Id", nativeQuery = true)
    List<songmodel> findSongById(@Param("Id") long Id);

    @Query(value = "SELECT s.id as track_id , s.track_num as track_num, s.track_name as track_name, " +
            "al.id as album_id, al.title as title, al.type as type, al.release_date as release_date, " +
            "a.id as artist_id, a.display_name as artist_name, a.email as email " +
            "FROM song as s " +
            "INNER JOIN album as al " +
            "ON s.album_id = al.id " +
            "INNER JOIN account as a " +
            "ON al.artist_id = a.id " +
            "WHERE a.role = 1 AND a.active = true AND s.active = true AND al.active = true AND s.album_id = :album_id", nativeQuery = true)
    List<songmodel> findSongByAlbum(@Param("album_id") long album_id);
    song save(song newSong);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE song as s " +
            "SET active = false " +
            "WHERE s.id = :Id "
            ,nativeQuery = true)
    void deleteSongById(@Param("Id") long Id);
}