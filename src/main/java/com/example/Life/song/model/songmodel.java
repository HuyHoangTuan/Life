package com.example.Life.song.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

public interface songmodel
{
    long getTrack_id();
    long getTrack_num();
    String getTrack_name();
    long getAlbum_id();
    String getTitle();
    long getType();
    Date getRelease_date();
    long getArtist_id();
    String getArtist_name();
}
