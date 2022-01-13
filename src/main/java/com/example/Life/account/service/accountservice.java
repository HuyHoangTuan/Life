package com.example.Life.account.service;


import com.example.Life.account.entity.account;
import com.example.Life.account.model.accountmodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface accountservice
{
    account Authenticate(String email, String password);
    boolean Register(String email, String password, String name,int role);

    List<?> getAllArtists();
    List<?> getAllUsers();

    accountmodel getArtist(long artist_id);
    accountmodel getUser(long user_id);

    account findAccount(long account_id);

    account save(account currentAccount);
}
