package com.example.Life.comment.model;

import java.sql.Date;
import java.sql.Timestamp;

public interface commentmodel
{
    long getId();
    long getPlaylist_id();
    long getAlbum_id();
    long getCreator_id();
    Date getCreated_timestamp();
    String getContent();
    boolean getActive();
}
