package com.example.Life.engine.search.controller;

import com.example.Life.JWT;
import com.example.Life.engine.search.service.searchservice;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class searchcontroller
{
    @Autowired
    private searchservice searchService;

    @GetMapping("/api/search/tracks")
    public ResponseEntity<?> searchTrack(@RequestParam(name = "token") String token, @RequestParam(name = "content") String content, @RequestParam(name = "index",defaultValue = "1") int index)
    {
        Claims claims = JWT.decodeJWT(token);
        if(claims == null)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong token\"}");
        String subject = claims.getSubject();
        if(subject == null)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong token\"}");
        return new ResponseEntity<>(searchService.searchSongWith(content, index), HttpStatus.OK);


    }
}