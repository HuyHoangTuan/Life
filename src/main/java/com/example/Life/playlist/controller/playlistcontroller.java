package com.example.Life.playlist.controller;

import com.example.Life.JWT;
import com.example.Life.playlist.model.playlistmodel;
import com.example.Life.playlist.model.songmodel;
import com.example.Life.playlist.model.songoutputmodel;
import com.example.Life.playlist.service.playlistservice;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class playlistcontroller
{
    @Autowired
    private playlistservice playlistService;

    @GetMapping("/api/users/{id}/playlists")
    public ResponseEntity<?> getAllPlaylistOfAnUser(@RequestParam(name = "token") String token,
                                                    @RequestParam(name = "index", defaultValue = "1",required = false) int index,
                                                    @PathVariable("id") long user_id)
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

        List<playlistmodel> allPlaylists = playlistService.getAllPlayListsOfUser(user_id);
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(allPlaylists.size()-1,index*perPage-1)+1;

        if(fromIndex>=toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);

        return new ResponseEntity<>(allPlaylists.subList(fromIndex, toIndex), HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}/playlists/{playlist_id}")
    public ResponseEntity<?> getPlaylistOfAnUser(@RequestParam(name = "token") String token,
                                                 @PathVariable("id") long user_id,
                                                 @PathVariable("playlist_id") long playlist_id)
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
        return new ResponseEntity<>(playlistService.getPlaylistOfUser(user_id,playlist_id), HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}/playlists/{playlist_id}/tracks")
    public ResponseEntity<?> getSongsOfPlaylist(@RequestParam(name = "token") String token,
                                                @RequestParam(name = "index", defaultValue = "1") int index,
                                                @PathVariable("id") long user_id,
                                                @PathVariable("playlist_id") long playlist_id)
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
        List<songoutputmodel> allSongs = playlistService.getAllSongsOfPlaylist(playlist_id);
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(allSongs.size()-1,index*perPage-1);
        if(fromIndex>toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);

        return new ResponseEntity<>(allSongs.subList(fromIndex, toIndex), HttpStatus.OK);
    }


}
