package com.example.Life.album.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class album
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "artist_id")
    private long artist_id;

    @Column(name = "release_date")
    private Date release_date;

    @Column(name = "type")
    private long type;

    @Column(name = "active")
    private boolean active;
}
