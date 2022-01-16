package com.example.Life.playlist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "playlist_song")
public class playlist_song
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "playlist_id")
    private long playlist_id;

    @Column(name = "song_id")
    private long song_id;

    @Column(name = "added_date")
    private Date added_date;

    @Column(name = "active")
    private boolean active;
}
