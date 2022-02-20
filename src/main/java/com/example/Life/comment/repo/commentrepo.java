package com.example.Life.comment.repo;

import com.example.Life.comment.entity.comment;
import com.example.Life.comment.model.commentmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface commentrepo extends JpaRepository<comment, UUID>
{
    @Query(value = "SELECT c.id as id, c.playlist_id as playlist_id, c.album_id as album_id " +
            ", c.created_timestamp as created_timestamp, c.content as content, c.active as active, c.creator_id as creator_id  " +
            "FROM comment as c " +
            "WHERE c.album_id = :album_id  AND c.active = true " +
            "ORDER BY created_timestamp ",nativeQuery = true)
    List<commentmodel> findAllCommentOfAlbum(@Param("album_id") long album_id);
    @Query(value = "SELECT c.id as id, c.playlist_id as playlist_id, c.album_id as album_id " +
            ", c.created_timestamp as created_timestamp, c.content as content, c.active as active, c.creator_id as creator_id  " +
            "FROM comment as c " +
            "WHERE c.playlist_id = :playlist_id  AND c.active = true " +
            "ORDER BY created_timestamp ",nativeQuery = true)
    List<commentmodel> findAllCommentOfPlaylist(@Param("playlist_id") long playlist_id);
    @Query(value = "SELECT c.id as id, c.playlist_id as playlist_id, c.album_id as album_id " +
            ", c.created_timestamp as created_timestamp, c.content as content, c.active as active, c.creator_id as creator_id " +
            "FROM comment as c " +
            "WHERE c.creator_id = :user_id  AND c.active = true " +
            "ORDER BY created_timestamp ",nativeQuery = true)
    List<commentmodel> findAllCommentOfUser(@Param("user_id") long user_id);

    @Query(value = "SELECT c.id as id, c.playlist_id as playlist_id, c.album_id as album_id " +
            ", c.created_timestamp as created_timestamp, c.content as content, c.active as active, c.creator_id as creator_id  " +
            "FROM comment as c " +
            "WHERE c.creator_id = :user_id AND c.playlist_id = :playlist_id  AND c.active = true " +
            "ORDER BY created_timestamp ",nativeQuery = true)
    List<commentmodel> findAllCommentOfUserInPlaylist(@Param("user_id") long user_id, @Param("playlist_id") long playlist_id);
    @Query(value = "SELECT c.id as id, c.playlist_id as playlist_id, c.album_id as album_id " +
            ", c.created_timestamp as created_timestamp, c.content as content, c.active as active, c.creator_id as creator_id  " +
            "FROM comment as c " +
            "WHERE c.creator_id = :user_id AND c.album_id = :album_id AND c.active = true " +
            "ORDER BY created_timestamp ",nativeQuery = true)
    List<commentmodel> findAllCommentOfUserInAlbum(@Param("user_id") long user_id, @Param("album_id") long album_id);

    comment save(comment c);
    comment findById(long comment_id);


}
