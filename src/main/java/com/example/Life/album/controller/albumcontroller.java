package com.example.Life.album.controller;

import com.example.Life.JWT;
import com.example.Life.LifeApplication;
import com.example.Life.album.entity.album;
import com.example.Life.album.model.albummodel;
import com.example.Life.album.model.newalbummodel;
import com.example.Life.album.service.albumservice;
import io.jsonwebtoken.Claims;
import javassist.bytecode.ByteArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.ws.rs.Path;
import javax.xml.ws.Response;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;


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
        List<?> allAlbum = albumService.getAllAlbums();
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(allAlbum.size()-1,index*perPage-1)+1;
        if(fromIndex>=toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);
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
        return new ResponseEntity<>(albumService.getAlbum(album_id), HttpStatus.OK);
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
        album newAlbum = new album();
        newAlbum.setType(albumModel.getType());
        newAlbum.setActive(true);
        newAlbum.setTitle(albumModel.getTitle());
        newAlbum.setArtist_id(artist_id);
        newAlbum.setRelease_date(albumModel.getRelease_date());
        albumService.save(newAlbum);

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

    @GetMapping("/api/albums/{id}/cover")
    public ResponseEntity<?> getAlbumCover(@RequestParam(name = "token") String token, @PathVariable("id") long album_id)
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

        albummodel currentAlbum = (albummodel) albumService.getAlbum(album_id);
        if(currentAlbum == null)
        {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong album_id\"}");
        }

        String path = LifeApplication.defaultDataDir+"\\"+currentAlbum.getArtist_id() +"\\"+currentAlbum.getAlbum_id()+"\\"+"cover.jpeg";
        File file = new File(path);
        BufferedImage image = null;
        try
        {
            image = ImageIO.read(file);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","image/jpeg")
                    .body(bytes);
        } catch (IOException e)
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"No image found\"}");
        }
    }

    @PostMapping("/api/albums/{id}/cover")
    public ResponseEntity<?> uploadCover(@RequestParam(name = "token") String token, @PathVariable("id") long album_id,
                                         @RequestParam("file") MultipartFile file)
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
            albummodel currentAlbum = (albummodel) albumService.getAlbum(album_id);

            InputStream inputStream = file.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            String path = LifeApplication.defaultDataDir+"\\"+Long.toString(currentAlbum.getArtist_id())+"\\"+Long.toString(currentAlbum.getAlbum_id())+"\\"+"cover.jpeg";
            File outputFile = new File(path);
            ImageIO.write(bufferedImage, "jpeg",outputFile);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"success\"}");
        } catch (IOException e)
        {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Something gone wrong\"}");
        }
    }

    @PutMapping("/api/albums/{id}")
    public ResponseEntity<?> editAlbum(@RequestParam(name = "token") String token, @PathVariable("id") long album_id,
                                       @RequestBody(required = false) Map<String, String> body)
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

        album currentAlbum = albumService.findAlbum(album_id);
        if(currentAlbum == null)
        {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong album_id\"}");
        }
        if(body == null)
        {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"No body found\"}");
        }
        if(body.get("active")!=null)
            currentAlbum.setActive(Boolean.parseBoolean(body.get("active")));
        if(body.get("title")!=null)
            currentAlbum.setTitle(body.get("title"));
        if(body.get("type")!=null)
            currentAlbum.setType(Long.parseLong(body.get("type")));
        albumService.save(currentAlbum);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"succes\"}");
    }
}
