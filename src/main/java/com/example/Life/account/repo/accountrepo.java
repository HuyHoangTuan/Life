package com.example.Life.account.repo;

import com.example.Life.account.entity.account;
import com.example.Life.account.model.accountmodel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface accountrepo extends JpaRepository<account, UUID>
{
    List<account> findById(long accountId);
    List<account> findByEmail(String email);
    account save(account Account);
    @Query(value = "SELECT a.display_name as display_name, " +
            "a.id as id, " +
            "a.email as email, " +
            "a.active as active, " +
            "a.role as role " +
            "FROM account as a " +
            "WHERE true = true " +
            "ORDER BY a.id" , nativeQuery = true)
    List<accountmodel> findAllAccounts();

    @Query(value = "SELECT a.display_name as display_name, " +
            "a.id as id, " +
            "a.email as email, " +
            "a.active as active, " +
            "a.role as role " +
            "FROM account as a " +
            "WHERE a.id=:account_id " +
            "ORDER BY a.id" , nativeQuery = true)
    List<accountmodel> findAccount(@Param("id") long account_id);

    /*
    @Query(value = "SELECT a.display_name as display_name, a.id as user_id, a.email as email, a.active as active, a.role as role " +
            "FROM account as a " +
            "WHERE a.role != 0" , nativeQuery = true)
    List<usermodel> findAllUsers();*/


}
