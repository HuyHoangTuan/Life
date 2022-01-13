package com.example.Life.account.repo;

import com.example.Life.account.entity.account;
import com.example.Life.account.model.artistmodel;
import com.example.Life.account.model.usermodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface accountrepo extends JpaRepository<account, UUID>
{
    List<account> findById(long accountId);
    List<account> findByEmail(String email);
    account save(account Account);
    @Query(value = "SELECT a.display_name as display_name, a.id as artist_id, a.email as email, a.active as active " +
            "FROM account as a " +
            "WHERE a.role = 1" , nativeQuery = true)
    List<artistmodel> findAllArtists();
    @Query(value = "SELECT a.display_name as display_name, a.id as user_id, a.email as email, a.active as active " +
            "FROM account as a " +
            "WHERE a.role = 2" , nativeQuery = true)
    List<usermodel> findAllUsers();


}
