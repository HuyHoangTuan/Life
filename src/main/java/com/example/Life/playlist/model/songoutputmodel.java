package com.example.Life.playlist.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter

public class songoutputmodel
{
    private boolean active;

    private long album_id;
    private long artist_id;
    private long playlist_id;
    private long track_id;

    private double duration;

    private String track_name;
    private String artist_name;
    private String tile;

    Date added_date;
}
