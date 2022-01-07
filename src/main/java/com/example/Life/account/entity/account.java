package com.example.Life.account.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class account
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "display_name")
    private String display_name;

    @Column(name = "active")
    private boolean active;

    @Column(name = "role")
    private int role;

    public account()
    {

    }
    public account(String email, String password, String display_name,int role)
    {
        this.email = email;
        this.password = password;
        this.display_name = display_name;
        this.role = role;
    }

}
