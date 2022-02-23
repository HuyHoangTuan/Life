package com.example.Life.comment.service;

import com.example.Life.comment.entity.comment;
import com.example.Life.comment.model.commentmodel;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
@Service
public interface commentservice
{
    List<commentmodel> getAllCommentOfAlbum(long album_id);
    List<commentmodel> getAllCommentOfPlaylist(long playlist_id);
    List<commentmodel> getAllCommentOfUser(long user_id);
    List<commentmodel> getAllCommentOfUserInAlbum(long user_id, long album_id);
    List<commentmodel> getALlCommentOfUserInPlaylist(long user_id, long playlist_id);

    commentmodel addCommentToAlbum(long user_id, long album_id, String content, Date time);
    commentmodel addCommentToPlaylist(long user_id, long playlist_id, String content, Date time);
    comment save(comment c);
    comment delete(comment c);
    comment getComment(long comment_id);

}
