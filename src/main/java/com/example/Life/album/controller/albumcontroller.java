package com.example.Life.album.controller;

import com.example.Life.JWT;
import com.example.Life.LifeApplication;
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
import java.util.List;


@RestController
public class albumcontroller
{
    @Autowired
    private albumservice albumService;

    @GetMapping("/api/albums")
    public ResponseEntity<?> getALlAlbums(@RequestParam(name ="token") String token, @RequestParam(name ="index", defaultValue = "1") int index)
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
        long artist_id = Long.parseLong(subject);
        List<?> allAlbum = albumService.getAllAlbum();
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(allAlbum.size()-1,index*perPage-1);
        if(fromIndex>toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(allAlbum.subList(fromIndex, toIndex));
    }
    @GetMapping("/api/albums/{id}/tracks")
    public ResponseEntity<?> getAllTracks(@RequestParam(name ="token") String token, @PathVariable("id") long albumId)
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
        try
        {
            return new ResponseEntity<>(albumService.getSongIn(albumId), HttpStatus.OK);
        } catch(Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }

    }
    @GetMapping("/api/albums/{id}")
    public ResponseEntity<?> getAlbum(@RequestParam(name = "token") String token,@PathVariable("id") long album_id)
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

        long artist_id = Long.parseLong(subject);
        return new ResponseEntity<>(albumService.getAlbum(album_id).get(0), HttpStatus.OK);
    }

    @PostMapping("/api/albums")
    public ResponseEntity<?> createNewAlbum(@RequestParam(name = "token") String token, @RequestBody newalbummodel albumModel)
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

        long artist_id = Long.parseLong(subject);
        album newAlbum = albumService.save(artist_id, albumModel.getTitle(), albumModel.getRelease_date(), albumModel.getType());
        String path = LifeApplication.defaultDataDir +"\\"+Long.toString(artist_id)+"\\"+Long.toString(newAlbum.getId());
        File file = new File(path);
        if(!file.exists()) file.mkdirs();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body(albumService.getAlbum(newAlbum.getId()));
    }

    @DeleteMapping("/api/albums/{id}")
    public ResponseEntity<?> deleteCurrentAlbum(@RequestParam(name = "token") String token, @PathVariable("id") long album_id)
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
        long artist_id = Long.parseLong(subject);

        album currentAlbum = albumService.delete(album_id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"OK\"}");

    }

    @GetMapping("/api/artists/{id}/albums")
    public ResponseEntity<?> getAllArtistAlbums(@RequestParam(name = "token") String token, @PathVariable("id") long artist_id)
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
        return new ResponseEntity<>(albumService.getArtistAlbum(artist_id), HttpStatus.OK);
    }
}
