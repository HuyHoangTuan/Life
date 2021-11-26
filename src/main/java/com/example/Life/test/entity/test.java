package com.example.Life.test.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
public class test
{
    @Id
    private String username;
}
