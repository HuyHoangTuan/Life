package com.example.Life.comment.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "comment")
public class comment
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "playlist_id")
    private long playlist_id;

    @Column(name = "album_id")
    private long album_id;

    @Column(name  = "creator_id")
    private long creator_id;

    @Column(name = "created_timestamp")
    private Date created_timestamp;

    @Column(name = "content")
    private String content;

    @Column(name = "active")
    private boolean active;

}
