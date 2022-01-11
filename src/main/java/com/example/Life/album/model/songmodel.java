package com.example.Life.album.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;


public interface songmodel
{
    public long getAlbum_id();
    public long getArtist_id();
    public long getTrack_num();
    public long getTrack_id();
    public String getTrack_name();
    public Date getRelease_date();
    public String getArtist_name();
}
