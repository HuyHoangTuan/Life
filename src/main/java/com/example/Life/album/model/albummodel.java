package com.example.Life.album.model;

import java.sql.Date;

public interface albummodel
{
    long getAlbum_id();
    String getTitle();
    long getArtist_id();
    String getArtist_name();
    Date getRelease_date();
    long getType();
    boolean getActive();
}
