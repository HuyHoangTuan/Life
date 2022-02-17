package com.example.Life.song.repo;

import com.example.Life.song.entity.favorite_song;
import com.example.Life.song.model.favsongmodel;
import com.example.Life.song.model.songmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface favorite_songrepo extends JpaRepository<favorite_song, UUID>
{
    @Query(value = "SELECT fs.id as id, fs.creator_id as user_id, fs.song_id as song_id, fs.active as active " +
            "FROM favorite_song as fs " +
            "WHERE fs.creator_id = :creator_id " +
            "", nativeQuery = true)
    List<favsongmodel> findAllFavorite_Song(@Param("creator_id") long creator_id);

    favorite_song save(favorite_song fs);
    favorite_song findById(long id);
    @Query(value = "SELECT fs.id as id, fs.creator_id as user_id, fs.song_id as song_id, fs.active as active " +
            "FROM favorite_song as fs " +
            "WHERE fs.creator_id = :creator_id AND fs.song_id = :song_id " +
            "", nativeQuery = true)
    List<favsongmodel> findFavoriteSongByUserIdAndSongId(@Param("creator_id") long user_id,@Param("song_id") long track_id);
}
