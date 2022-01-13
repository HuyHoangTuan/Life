package com.example.Life.engine.search.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface searchservice
{
    List<?> searchSongWith(String content, int index);
}
