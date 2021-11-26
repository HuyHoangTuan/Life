package com.example.Life.test.service;

import com.example.Life.test.entity.test;
import com.example.Life.test.repo.testRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class testServiceImpl implements testService
{
    @Autowired
    private testRepo repo;

    @Override
    public List<test> findAll() {
        return repo.findAll();
    }
}
