package com.example.Life.account.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "favorite_artist")
public class favorite_artist
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "creator_id")
    private long creator_id;

    @Column(name = "artist_id")
    private long artist_id;

    @Column(name = "active")
    private boolean active;
}
