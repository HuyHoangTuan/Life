package com.example.Life.album.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "favorite_album")
public class favorite_album
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "creator_id")
    private long creator_id;

    @Column(name = "album_id")
    private long album_id;

    @Column(name = "active")
    private boolean active;
}
