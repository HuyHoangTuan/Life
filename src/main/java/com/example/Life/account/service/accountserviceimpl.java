package com.example.Life.account.service;

import com.example.Life.LifeApplication;
import com.example.Life.account.entity.account;
import com.example.Life.account.model.accountmodel;
import com.example.Life.account.repo.accountrepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<accountmodel> allArtists = getAllArtists();
        for(accountmodel current: allArtists)
        {
            if(current.getId() == artist_id) return current;
        }
        return null;
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
}
