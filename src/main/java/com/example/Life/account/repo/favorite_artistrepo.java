package com.example.Life.account.repo;

import com.example.Life.account.entity.favorite_artist;
import com.example.Life.account.model.favartistmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface favorite_artistrepo extends JpaRepository<favorite_artist, UUID>
{

    @Query(value = "SELECT fa.id as id, fa.creator_id as user_id, fa.artist_id as artist_id, fa.active as active  " +
            "FROM favorite_artist as fa " +
            "WHERE fa.creator_id = :user_id " +
            ""
            ,nativeQuery = true)
    List<favartistmodel> findALlFavArtist(@Param("user_id") long user_id);

    @Query(value = "SELECT fa.id as id, fa.creator_id as user_id, fa.artist_id as artist_id, fa.active as active  " +
            "FROM favorite_artist as fa " +
            "WHERE fa.creator_id = :user_id AND fa.artist_id = :artist_id " +
            ""
            ,nativeQuery = true)
    List<favartistmodel> findFavoriteArtistByUserIdAndArtistId(@Param("user_id") long user_id, @Param("artist_id") long artist_id);
    favorite_artist save(favorite_artist fa);
    favorite_artist findById(long id);
}
