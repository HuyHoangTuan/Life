package com.example.Life.song.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class songoutputmodel
{
    String title;
    String artist_name;
    Date release_date;
    String track_name;
    long album_id;
    long track_id;
    long artist_id;
    long track_num;
    double duration;
    boolean active;

}
