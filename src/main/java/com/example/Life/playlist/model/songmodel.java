package com.example.Life.playlist.model;

import java.sql.Date;

public interface songmodel
{
    boolean getActive();

    long getPlaylist_id();
    long getTrack_id();
    long getAlbum_id();
    long getArtist_id();
    long getTrack_num();

    String getArtist_name();
    String getTitle();
    String getTrack_name();

    Date getAdded_date();
}
