package com.example.Life.account.service;


import com.example.Life.account.entity.account;
import com.example.Life.account.model.artistmodel;
import com.example.Life.account.model.usermodel;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service

public interface accountservice
{
    account Authenticate(String email, String password);
    boolean Register(String email, String password, String name,int role);
    List<?> getAllArtists();
    List<?> getAllUsers();
    artistmodel findArtist(long artist_id);
    usermodel findUser(long user_id);
    account findAccount(long account_id);
    account save(account currentAccount);
}
