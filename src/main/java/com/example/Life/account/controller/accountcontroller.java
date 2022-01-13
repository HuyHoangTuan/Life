package com.example.Life.account.controller;

import com.example.Life.JWT;
import com.example.Life.LifeApplication;
import com.example.Life.account.entity.account;
import com.example.Life.account.model.*;
import com.example.Life.account.service.accountservice;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> getAllArtists(@RequestParam(name = "token") String token, @RequestParam(name = "index", defaultValue = "1") int index)
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
        List<?> listArtists = accountService.getAllArtists();
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(listArtists.size()-1,index*perPage-1);
        if(fromIndex>toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);
        return new ResponseEntity<>(listArtists.subList(fromIndex, toIndex), HttpStatus.OK);
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

    @GetMapping("/api/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(name = "token") String token, @RequestParam(name = "index", defaultValue = "1") int index)
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
        List<?> listUsers = accountService.getAllUsers();
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(listUsers.size()-1,index*perPage-1);
        if(fromIndex>toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);
        return new ResponseEntity<>(listUsers.subList(fromIndex, toIndex), HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<?> getUser(@RequestParam(name = "token") String token, @PathVariable("id") long user_id)
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

        return new ResponseEntity<>(accountService.findUser(user_id), HttpStatus.OK);
    }

    @PostMapping("/api/me/users/{id}")
    public ResponseEntity<?> editUser(
            @RequestParam(name = "token") String token, @PathVariable("id") long user_id, @RequestBody accountmodel accountModel)
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

        account currentAccount = accountService.findAccount(user_id);
        if(currentAccount == null || currentAccount.getRole()!=LifeApplication.USER)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong user_id\"}");

        currentAccount.setActive(accountModel.isActive());
        currentAccount.setDisplay_name(accountModel.getDisplay_name());
        currentAccount.setPassword(accountModel.getPassword());
        accountService.save(currentAccount);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }


    @PostMapping("/api/me/artists/{id}")
    public ResponseEntity<?> editArtist(
            @RequestParam(name = "token") String token, @PathVariable("id") long artist_id, @RequestBody accountmodel accountModel)
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

        account currentAccount = accountService.findAccount(artist_id);
        if(currentAccount == null || currentAccount.getRole()!= LifeApplication.ARTIST)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong artist_id\"}");

        currentAccount.setActive(accountModel.isActive());
        currentAccount.setDisplay_name(accountModel.getDisplay_name());
        currentAccount.setPassword(accountModel.getPassword());
        accountService.save(currentAccount);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }
}

