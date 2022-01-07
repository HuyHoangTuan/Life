package com.example.Life.test.controller;

import com.example.Life.test.service.testService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.Principal;

@RestController
public class controller
{
    @Autowired
    private testService service;

    @GetMapping("/api")
    public ResponseEntity<?> gettest(@RequestParam(name ="c") String content)
    {
        for(int i=0;i<content.length();i++)
        {
            System.out.println((int)content.charAt(i));
        }
        System.out.println("------------------");
        return new ResponseEntity<>(content, HttpStatus.OK);
    }
}
