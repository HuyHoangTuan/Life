package com.example.Life.account.service;


import com.example.Life.account.entity.account;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public interface accountservice
{
    public account Authenticate(String email, String password);
    public boolean Register(String email, String password, String name,int role);
}
