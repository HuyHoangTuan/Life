package com.example.Life.album.controller;

import com.example.Life.JWT;
import com.example.Life.album.entity.album;
import com.example.Life.album.model.albummodel;
import com.example.Life.album.model.newalbummodel;
import com.example.Life.album.service.albumservice;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;


@RestController
public class albumcontroller
{
    private final String defaultDir = "F:\\Life\\Back-End\\src\\main\\java\\com\\example\\Life\\data\\static" ;

    @Autowired
    private albumservice albumService;

    @GetMapping("/{token}/api/{artist_id}/album/track")
    public ResponseEntity<?> getAllTrack(@PathVariable("token") String token,@PathVariable("artist_id") long artist_id, @RequestParam("id") long albumId)
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
        return new ResponseEntity<>(albumService.getSongIn(albumId), HttpStatus.OK);
    }
    @GetMapping("/{token}/api/{artist_id}/album")
    public ResponseEntity<?> getAllAlbum(@PathVariable("token") String token,@PathVariable("artist_id") long artist_id, @RequestParam(value = "id", required = false) Long id)
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
        if(id == null)
            return new ResponseEntity<>(albumService.getAlbum(artist_id),HttpStatus.OK);
        else
            return new ResponseEntity<>(albumService.getAlbum(artist_id, id), HttpStatus.OK);
    }
    @GetMapping("/{token}/api/{artist_id}/album/create")
    public ResponseEntity<?> createNewAlbum(@PathVariable("token") String token, @PathVariable("artist_id") long artist_id, @RequestBody newalbummodel albumModel)
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

        album newAlbum = albumService.save(albumModel);
        String path = defaultDir+"\\"+Long.toString(artist_id)+"\\"+Long.toString(newAlbum.getId());
        File file = new File(path);
        if(!file.exists()) file.mkdirs();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body(albumService.getAlbum(artist_id, newAlbum.getId()));
    }
    @GetMapping("/{token}/api/{artist_id}/album/delete")
    public ResponseEntity<?> deleteCurrentAlbum(@PathVariable("token") String token, @PathVariable("artist_id") long artist_id, @RequestParam("id") long albumId)
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
        long account_id = Long.parseLong(subject);
        if(account_id !=1 && account_id != artist_id)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"This account can not delete the album.\"}");

        album currentAlbum = albumService.delete(albumId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"OK\"}");

    }
}
