package com.example.Life.test.repo;

import com.example.Life.test.entity.test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface testRepo extends JpaRepository<test, String>
{
    List<test> findAll();
    List<test> findByUsername(String username);
}
