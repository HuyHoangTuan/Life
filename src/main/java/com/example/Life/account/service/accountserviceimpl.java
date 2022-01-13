package com.example.Life.account.service;

import com.example.Life.account.entity.account;
import com.example.Life.account.model.artistmodel;
import com.example.Life.account.model.usermodel;
import com.example.Life.account.repo.accountrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class accountserviceimpl implements accountservice
{
    @Autowired
    private accountrepo accountRepo;

    @Override
    public account Authenticate(String email, String password)
    {
        List<account> validAccount = accountRepo.findByEmail(email);
        if(validAccount.size() == 0) return null;
        if(!password.equals(validAccount.get(0).getPassword())) return null;
        return validAccount.get(0);
    }

    @Override
    public boolean Register(String email, String password, String name,int role)
    {
        List<account> validEmail = accountRepo.findByEmail(email);
        if(validEmail.size()!=0) return false;
        account newAccount = new account(email, password, name,role);
        accountRepo.save(newAccount);
        return true;
    }

    @Override
    public List<?> getAllArtists()
    {
        return accountRepo.findAllArtists();
    }

    @Override
    public List<?> getAllUsers() {
        return accountRepo.findAllUsers();
    }

    @Override
    public artistmodel findArtist(long artist_id)
    {
        List<artistmodel> allArtists = accountRepo.findAllArtists();
        for(artistmodel current: allArtists)
        {
            if(current.getArtist_id() == artist_id) return current;
        }
        return null;
    }

    @Override
    public usermodel findUser(long user_id)
    {
        List<usermodel> allUsers = accountRepo.findAllUsers();
        for( usermodel current: allUsers)
        {
            if(current.getUser_id() == user_id) return current;
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
}
