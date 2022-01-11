package com.example.Life.account.controller;

import com.example.Life.JWT;
import com.example.Life.account.entity.account;
import com.example.Life.account.model.loginmodel;
import com.example.Life.account.model.registermodel;
import com.example.Life.account.model.responseloginmodel;
import com.example.Life.account.model.responseregistermodel;
import com.example.Life.account.service.accountservice;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class accountcontroller
{
    @Autowired
    private accountservice accountService;

    @PostMapping(path = "/api/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody loginmodel loginModel)
    {
        account authentication = accountService.Authenticate(loginModel.getEmail(), loginModel.getPassword());
        responseloginmodel responseLoginModel;
        if(authentication == null) return new ResponseEntity<>( responseLoginModel = new responseloginmodel("",""), HttpStatus.OK);
        responseLoginModel = new responseloginmodel(JWT.createJWT(Long.toString(authentication.getId())), authentication.getDisplay_name());
        return new ResponseEntity<>(responseLoginModel, HttpStatus.OK);
    }

    @PostMapping(path = "/api/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody registermodel registerModel)
    {
        responseregistermodel responseRegisterModel;
        if(!accountService.Register(registerModel.getEmail(),registerModel.getPassword(),registerModel.getDisplay_name(),registerModel.getRole()))
            return new ResponseEntity<>(responseRegisterModel = new responseregistermodel("Email has been used"), HttpStatus.OK);
        responseRegisterModel =  new responseregistermodel("Successfully registered for account name: "+registerModel.getDisplay_name()+"!");
        return new ResponseEntity<>(responseRegisterModel, HttpStatus.OK);
    }

    @GetMapping("/api/artists")
    public ResponseEntity<?> getAllArtist(@RequestParam(name = "token") String token)
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
        long admin_id = Long.parseLong(subject);
        if(admin_id != 1)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong token\"}");
        return new ResponseEntity<>(accountService.getAllArtist(), HttpStatus.OK);
    }
    @GetMapping("/api/artists/{id}")
    public ResponseEntity<?> getArtist(@RequestParam(name = "token") String token, @PathVariable("id") long artist_id)
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
        return new ResponseEntity<>(accountService.findArtist(artist_id), HttpStatus.OK);
    }
}
