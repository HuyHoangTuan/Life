package com.example.Life.album.repo;

import com.example.Life.album.entity.album;
import com.example.Life.album.model.albummodel;
import com.example.Life.album.model.songmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface albumrepo extends JpaRepository<album, UUID>
{
    @Query( value = "SELECT al.id as album_id, al.title as title, al.active as active, " +
            "a.id as artist_id, a.display_name as artist_name, " +
            "al.release_date as release_date, al.type as type " +
            "FROM album as al " +
            "INNER JOIN account as a " +
            "ON al.artist_id = a.id " +
            "WHERE al.id = :albumId "
            ,nativeQuery = true)
    List<albummodel> findAlbumById( @Param("albumId") long albumId);

    @Query( value = "SELECT al.id as album_id, al.title as title, al.active as active, " +
            "a.id as artist_id, a.display_name as artist_name, " +
            "al.release_date as release_date, al.type as type " +
            "FROM album as al " +
            "INNER JOIN account as a " +
            "ON al.artist_id = a.id " +
            "WHERE a.id = :artistId " +
            "ORDER BY al.id"
            ,nativeQuery = true)
    List<albummodel> findAlbumByArtistId(@Param("artistId") long artistId);

    @Query( value = "SELECT al.id as album_id, al.title as title, al.active as active, " +
            "a.id as artist_id, a.display_name as artist_name, " +
            "al.release_date as release_date, al.type as type " +
            "FROM album as al " +
            "INNER JOIN account as a " +
            "ON al.artist_id = a.id " +
            "WHERE true = true " +
            "ORDER BY al.id"
            ,nativeQuery = true)
    List<albummodel> findAllAlbum();
    @Query( value = "SELECT al.id as album_id, al.title as title, al.active as active, " +
            "a.id as artist_id, a.display_name as artist_name, " +
            "al.release_date as release_date, al.type as type " +
            "FROM album as al " +
            "INNER JOIN account as a " +
            "ON al.artist_id = a.id " +
            "WHERE al.id = album_id " +
            "ORDER BY al.id"
            ,nativeQuery = true)
    List<albummodel> findAlbum(@Param("album_id") long album_id);

    @Query( value = "SELECT s.track_num as track_num, s.track_name as track_name, al.active as active, " +
            "al.id as album_id, al.release_date as release_date, s.id as track_id," +
            "a.display_name as artist_name, a.id as artist_id " +
            "FROM song as s " +
            "INNER JOIN album as al " +
            "ON al.id = s.album_id " +
            "INNER JOIN account as a " +
            "ON a.id = al.artist_id " +
            "WHERE al.id = :albumId"
            , nativeQuery = true)
    List<songmodel> findSongIn(@Param("albumId") long albumId);

    album save(album Album);
    album findById(long albumId);
}
