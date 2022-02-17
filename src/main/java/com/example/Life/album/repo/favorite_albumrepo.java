package com.example.Life.album.repo;

import com.example.Life.album.entity.favorite_album;
import com.example.Life.album.model.favalbummodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface favorite_albumrepo extends JpaRepository<favorite_album, UUID>
{

    @Query(value = "SELECT fa.id as id, fa.creator_id as user_id, fa.album_id as album_id, fa.active as active " +
            "FROM favorite_album as fa " +
            "WHERE fa.creator_id = :user_id " +
            ""
            ,nativeQuery = true)
    List<favalbummodel> findALlFavAlbum(@Param("user_id") long user_id);

    @Query(value = "SELECT fa.id as id, fa.creator_id as user_id, fa.album_id as album_id, fa.active as active  " +
            "FROM favorite_album as fa " +
            "WHERE fa.creator_id = :user_id AND fa.album_id = :album_id " +
            ""
            ,nativeQuery = true)
    List<favalbummodel> findFavoriteAlbumByUserIdAndAlbumId(@Param("user_id") long user_id, @Param("album_id") long album_id);
    favorite_album save(favorite_album fa);
    favorite_album findById(long id);
}
