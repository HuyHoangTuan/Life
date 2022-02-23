package com.example.Life.playlist.repo;

import com.example.Life.playlist.entity.playlist_song;
import com.example.Life.playlist.model.playlist_songmodel;
import com.example.Life.playlist.model.songmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface playlist_songrepo extends JpaRepository<playlist_song, UUID>
{
    playlist_song save(playlist_song ps);
    playlist_song findById(long ps);
    @Query(value = "SELECT pls.id as id " +
            "FROM playlist_song as pls " +
            "WHERE pls.playlist_id = :playlist_id AND pls.song_id = :song_id " +
            "ORDER BY pls.id ",
            nativeQuery = true)
    List<playlist_songmodel> findPlaylistSong(@Param("playlist_id") long playlist_id,@Param("song_id") long song_id);

    @Query(value = "SELECT pls.active as active, pls.playlist_id as playlist_id, " +
            "pls.added_date as added_date, " +
            "s.id as track_id, s.track_name as track_name, s.track_num as track_num, " +
            "al.id as album_id, al.title as title, " +
            "a.id as artist_id, a.display_name as artist_name " +
            "FROM playlist_song as pls " +
            "INNER JOIN song as s " +
            "ON s.id = pls.song_id " +
            "INNER JOIN album as al " +
            "ON al.id = s.album_id " +
            "INNER JOIN account as a " +
            "ON al.artist_id = a.id " +
            "WHERE pls.playlist_id = :playlist_id AND s.active = true " +
            "ORDER BY pls.id ",
            nativeQuery = true)
    List<songmodel> findAllSongsOfPlaylist(@Param("playlist_id") long playlist_id);
}
