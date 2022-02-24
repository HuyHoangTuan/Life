package com.example.Life.account.service;

import com.example.Life.LifeApplication;
import com.example.Life.account.entity.account;
import com.example.Life.account.entity.favorite_artist;
import com.example.Life.account.model.accountmodel;
import com.example.Life.account.model.favartistmodel;
import com.example.Life.account.repo.accountrepo;
import com.example.Life.account.repo.favorite_artistrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class accountserviceimpl implements accountservice
{
    @Autowired
    private accountrepo accountRepo;

    @Autowired
    private favorite_artistrepo favoriteArtistrepo;
    @Override
    public account Authenticate(String email, String password)
    {
        List<account> validAccount = accountRepo.findByEmail(email);
        if(validAccount.size() == 0) return null;
        if(!password.equals(validAccount.get(0).getPassword())) return null;
        if(validAccount.get(0).isActive() == false) return null;
        return validAccount.get(0);
    }

    @Override
    public boolean Register(String email, String password, String name,int role)
    {
        List<account> validEmail = accountRepo.findByEmail(email);
        if(validEmail.size()!=0) return false;
        account newAccount = new account(email, password, name,role);
        newAccount.setActive(true);
        accountRepo.save(newAccount);
        return true;
    }

    @Override
    public List<accountmodel> getAllArtists()
    {
        List<accountmodel> listAccounts = accountRepo.findAllAccounts();
        List<accountmodel> listArtists = new ArrayList<>();
        for(accountmodel current: listAccounts)
        {
            if(current.getRole() == LifeApplication.ARTIST) listArtists.add(current);
        }
        return listArtists;
    }
    @Override
    public accountmodel getArtist(long artist_id)
    {
        return accountRepo.findAccount(artist_id).get(0);
        /*List<accountmodel> allArtists = getAllArtists();
        for(accountmodel current: allArtists)
        {
            if(current.getId() == artist_id) return current;
        }
        return null;*/
    }

    @Override
    public List<accountmodel> getAllUsers()
    {
        List<accountmodel> listAccounts = accountRepo.findAllAccounts();
        List<accountmodel> listUsers = new ArrayList<>();
        for(accountmodel current: listAccounts)
        {
            if( true == true) listUsers.add(current);
        }
        return listUsers;
    }
    @Override
    public accountmodel getUser(long user_id)
    {
        List<accountmodel> allUsers = getAllUsers();
        for( accountmodel current: allUsers)
        {
            if(current.getId() == user_id) return current;
        }
        return null;
    }

    @Override
    public account findAccount(long account_id)
    {
        if( accountRepo.findById(account_id).size() == 0) return null;
        return accountRepo.findById(account_id).get(0);
    }

    @Override
    public account save(account currentAccount)
    {
        return accountRepo.save(currentAccount);
    }

    @Override
    public List<accountmodel> getAllFavArtist(long user_id)
    {
        List<favartistmodel> listFavArtist = favoriteArtistrepo.findALlFavArtist(user_id);
        List<accountmodel> output = new ArrayList<>();
        for(favartistmodel current : listFavArtist)
        {
            if(current.getActive() == false) continue;
            output.add(getArtist(current.getArtist_id()));
        }
        return output;
    }

    @Override
    public favorite_artist addNewFavArtist(long user_id, long artist_id)
    {
        List<favartistmodel> listFavArtist = favoriteArtistrepo.findFavoriteArtistByUserIdAndArtistId(user_id, artist_id);
        for(favartistmodel current : listFavArtist)
        {
            favorite_artist fa = favoriteArtistrepo.findById(current.getId());
            fa.setActive(true);
            return favoriteArtistrepo.save(fa);
        }
        favorite_artist fa = new favorite_artist();
        fa.setArtist_id(artist_id);
        fa.setCreator_id(user_id);
        fa.setActive(true);
        return favoriteArtistrepo.save(fa);
    }

    @Override
    public favorite_artist deleteFavArtist(long user_id, long artist_id)
    {
        List<favartistmodel> listFavArtist = favoriteArtistrepo.findFavoriteArtistByUserIdAndArtistId(user_id, artist_id);
        for(favartistmodel current : listFavArtist)
        {
            favorite_artist fa = favoriteArtistrepo.findById(current.getId());
            fa.setActive(false);
            return favoriteArtistrepo.save(fa);
        }
        return null;
    }
}
