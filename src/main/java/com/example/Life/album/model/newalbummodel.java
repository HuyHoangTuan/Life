package com.example.Life.album.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
public class newalbummodel
{
    private String title;
    private Date release_date;
    private long type;
    private boolean active;
}
