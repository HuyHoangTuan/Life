package com.example.Life.test.controller;

import com.example.Life.test.service.testService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class controller
{
    @Autowired
    private testService service;

    @GetMapping("/")
    public ResponseEntity<?> gettest(Principal principal)
    {
        return ResponseEntity.ok().body(service.findAll().get(0).getUsername());
    }
}
