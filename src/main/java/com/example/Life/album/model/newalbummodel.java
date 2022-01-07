package com.example.Life.album.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
public class newalbummodel
{
    private long artist_id;
    private String title;
    private Date release_date;
    private long type;
}
