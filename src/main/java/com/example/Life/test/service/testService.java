package com.example.Life.test.service;

import com.example.Life.test.entity.test;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface testService
{
    List<test> findAll();

}
