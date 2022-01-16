package com.example.Life.playlist.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "playlist")
public class playlist
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "creator_id")
    private long creator_id;

    @Column(name = "active")
    private long active;

    @Column(name = "title")
    private String title;
}
