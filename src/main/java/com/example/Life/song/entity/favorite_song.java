package com.example.Life.song.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "favorite_song")
public class favorite_song
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "creator_id")
    private long creator_id;

    @Column(name = "song_id")
    private long songg_id;

    @Column(name = "active")
    private boolean active;
}
