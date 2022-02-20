package com.example.Life.comment.service;

import com.example.Life.comment.entity.comment;
import com.example.Life.comment.model.commentmodel;
import com.example.Life.comment.repo.commentrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
@Service
public class commentserviceimpl implements commentservice
{
    @Autowired
    private commentrepo commentRepo;
    @Override
    public List<commentmodel> getAllCommentOfAlbum(long album_id)
    {
        return commentRepo.findAllCommentOfAlbum(album_id);
    }

    @Override
    public List<commentmodel> getAllCommentOfPlaylist(long playlist_id) {
        return commentRepo.findAllCommentOfPlaylist(playlist_id);
    }

    @Override
    public List<commentmodel> getAllCommentOfUser(long user_id) {
        return commentRepo.findAllCommentOfUser(user_id);
    }

    @Override
    public List<commentmodel> getAllCommentOfUserInAlbum(long user_id, long album_id)
    {
        return commentRepo.findAllCommentOfUserInAlbum(user_id, album_id);
    }

    @Override
    public List<commentmodel> getALlCommentOfUserInPlaylist(long user_id, long playlist_id) {
        return commentRepo.findAllCommentOfUserInPlaylist(user_id,playlist_id);
    }

    @Override
    public comment addCommentToAlbum(long user_id, long album_id, String content, Date time)
    {
        comment c = new comment();
        c.setCreator_id(user_id);
        c.setAlbum_id(album_id);
        c.setContent(content);
        c.setCreated_timestamp(time);
        c.setActive(true);
        commentRepo.save(c);
        return c;
    }

    @Override
    public comment addCommentToPlaylist(long user_id, long playlist_id, String content, Date time)
    {
        comment c = new comment();
        c.setCreator_id(user_id);
        c.setPlaylist_id(playlist_id);
        c.setContent(content);
        c.setCreated_timestamp(time);
        c.setActive(true);
        commentRepo.save(c);
        return c;
    }

    @Override
    public comment getComment(int comment_id)
    {
        return commentRepo.findById(comment_id);
    }

    @Override
    public comment save(comment c)
    {
        return commentRepo.save(c);
    }

    @Override
    public comment delete(comment c)
    {
        c.setActive(false);
        return commentRepo.save(c);
    }
}
