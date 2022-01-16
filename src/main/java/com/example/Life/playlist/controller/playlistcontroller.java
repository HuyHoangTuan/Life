package com.example.Life.playlist.controller;

import com.example.Life.JWT;
import com.example.Life.playlist.entity.playlist;
import com.example.Life.playlist.entity.playlist_song;
import com.example.Life.playlist.model.playlistmodel;
import com.example.Life.playlist.model.songoutputmodel;
import com.example.Life.playlist.service.playlistservice;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@RestController
public class playlistcontroller
{
    @Autowired
    private playlistservice playlistService;

    @GetMapping("/api/playlists")
    public ResponseEntity<?> getAllPlaylists(@RequestParam(name = "token") String token,
                                             @RequestParam(name = "index", defaultValue = "1", required = false) int index)
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
        List<playlistmodel> allPlaylists = playlistService.getAllPlayLists();
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(allPlaylists.size()-1,index*perPage-1)+1;

        if(fromIndex>=toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);

        return new ResponseEntity<>(allPlaylists.subList(fromIndex, toIndex), HttpStatus.OK);
    }
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
    @PostMapping("/api/users/{id}/playlists")
    public ResponseEntity<?> createPlaylist(@RequestParam(name = "token") String token,
                                            @PathVariable("id") long user_id,
                                            @RequestBody Map<String, String> body)
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

        playlist newPlaylist = new playlist();
        if(body.get("title")!=null)
            newPlaylist.setTitle(body.get("title"));
        newPlaylist.setCreator_id(user_id);
        newPlaylist.setActive(true);
        playlistService.save(newPlaylist);
        return new ResponseEntity<>(newPlaylist, HttpStatus.OK);
    }
    @PutMapping("/api/users/{id}/playlists/{playlist_id}")
    public ResponseEntity<?> editPlaylist(@RequestParam(name = "token") String token,
                                         @PathVariable("id") long user_id,
                                         @PathVariable("playlist_id") long playlist_id,
                                         @RequestBody Map<String, String> body)
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

        playlist current = playlistService.getPlaylist(playlist_id);
        if(body.get("title")!=null)
            current.setTitle(body.get("title"));
        return new ResponseEntity<>(playlistService.save(current), HttpStatus.OK);

    }
    @DeleteMapping("/api/users/{id}/playlists/{playlist_id}")
    public ResponseEntity<?> deletePlaylist(@RequestParam(name = "token") String token,
                                            @PathVariable("id") long user_id,
                                            @PathVariable("play_list") long playlist_id)
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
        playlist current = playlistService.getPlaylist(playlist_id);
        current.setActive(false);
        return new ResponseEntity<>(playlistService.save(current), HttpStatus.OK);

    }
    @PostMapping("/api/users/{id}/playlists/{playlist_id}/tracks/{track_id}")
    public ResponseEntity<?> addSong2Playlist(@RequestParam(name = "token") String token,
                                                @PathVariable("id") long user_id,
                                                @PathVariable("playlist_id") long playlist_id,
                                                @PathVariable("track_id") long track_id,
                                                @RequestBody Map<String, String> body)
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

        playlist_song newPlaylist_song = new playlist_song();
        newPlaylist_song.setPlaylist_id(playlist_id);
        newPlaylist_song.setSong_id(track_id);
        newPlaylist_song.setActive(true);
        try
        {
            if(body.get("added_date")!=null)
                newPlaylist_song.setAdded_date(Date.valueOf(body.get("added_date")));
        } catch (Exception e)
        {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        System.out.println(1);
        return new ResponseEntity<>(playlistService.save(newPlaylist_song), HttpStatus.OK);
    }
    @DeleteMapping("/api/users/{id}/playlists/{playlist_id}/tracks/{track_id}")
    public ResponseEntity<?>deleteSongInPlaylist(@RequestParam(name = "token") String token,
                                              @PathVariable("id") long user_id,
                                              @PathVariable("playlist_id") long playlist_id,
                                              @PathVariable("track_id") long track_id)
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

        playlist_song current = playlistService.getPlaylistSong(playlist_id, track_id);
        current.setActive(false);
        return new ResponseEntity<>(playlistService.save(current), HttpStatus.OK);
    }

}
