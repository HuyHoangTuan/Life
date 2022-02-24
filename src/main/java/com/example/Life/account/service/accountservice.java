package com.example.Life.account.service;


import com.example.Life.account.entity.account;
import com.example.Life.account.entity.favorite_artist;
import com.example.Life.account.model.accountmodel;
import com.example.Life.account.model.favartistmodel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface accountservice
{
    account Authenticate(String email, String password);
    boolean Register(String email, String password, String name,int role);

    List<?> getAllArtists();
    List<accountmodel> getAllUsers();

    accountmodel getArtist(long artist_id);
    accountmodel getUser(long user_id);

    account findAccount(long account_id);

    account save(account currentAccount);

    List<accountmodel> getAllFavArtist(long user_id);
    favorite_artist addNewFavArtist(long user_id, long artist_id);
    favorite_artist deleteFavArtist(long user_id, long artist_id);
}
