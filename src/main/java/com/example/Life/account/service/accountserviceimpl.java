package com.example.Life.account.service;

import com.example.Life.account.entity.account;
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
}
