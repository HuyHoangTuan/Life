package com.example.Life.account.repo;

import com.example.Life.account.entity.account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface accountrepo extends JpaRepository<account, UUID>
{
    List<account> findById(long accountId);
    List<account> findByEmail(String email);
    account save(account Account);
}
