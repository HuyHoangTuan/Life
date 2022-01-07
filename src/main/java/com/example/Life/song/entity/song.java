package com.example.Life.song.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
public class song
{
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "track_name")
    private String track_name;

    @Column(name = "album_id")
    private long album_id;

    @Column(name = "track_num")
    private long track_num;

    @Column(name = "active")
    private boolean active;

}
