package com.example.Life.song.controller;

import com.example.Life.JWT;
import com.example.Life.song.entity.song;
import com.example.Life.song.model.songmodel;
import com.example.Life.song.model.songoutputmodel;
import com.example.Life.song.service.songservice;

import io.jsonwebtoken.Claims;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileFormat;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.hibernate.result.Output;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class songcontroller
{
    private final int BYTE_RANGE = 128;
    private final String defaultSongDir = "F:\\Life\\Back-End\\src\\main\\java\\com\\example\\Life\\data\\static" ;
    /// artistId -> albumId -> trackNum
    @Autowired
    private songservice songService;

    @GetMapping("/{token}/api/track/search")
    public ResponseEntity<?> searchSong(@PathVariable("token") String token, @RequestParam(name = "c") String content)
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
        return new ResponseEntity<>(songService.findSong(content), HttpStatus.OK);
    }

    @GetMapping("/api/tracks/{id}")
    public ResponseEntity<?> getSongDuration(@RequestParam(name = "token") String token, @PathVariable("id") long song_id)
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
        return new ResponseEntity<>(songService.getSong(song_id), HttpStatus.OK);

    }


    @GetMapping("/api/tracks/{id}/audio")
    public ResponseEntity<?> streamingAudio(@RequestParam(name = "token") String token
            ,@PathVariable("id") long song_id) throws IOException
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

        songoutputmodel currentSong = songService.getSong(song_id);

        if(currentSong == null)
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("\"status\":\"Wrong song id\"");

        String songPath = "\\"
                +Long.toString(currentSong.getArtist_id())+"\\"
                +Long.toString(currentSong.getAlbum_id())+"\\"
                +Long.toString(currentSong.getTrack_num())+".mp3";
        long fileSize = 0;
        byte[] data;
        Path path = Paths.get(defaultSongDir+songPath);
        try (InputStream inputStream = (Files.newInputStream(path));
             ByteArrayOutputStream bufferOutputStream = new ByteArrayOutputStream())
        {
            byte[] newData = new byte[BYTE_RANGE];
            int nRead;
            while( (nRead = inputStream.read(newData, 0, newData.length))!=-1 )
            {
                bufferOutputStream.write(newData,0, nRead);
                fileSize+=nRead;
            }
            bufferOutputStream.flush();
            data = new byte[(int)fileSize];
            System.arraycopy(bufferOutputStream.toByteArray(), 0,data,0, data.length);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","audio/mpeg")
                .header("Accept-Ranges","bytes")
                .header("Content-Length", String.valueOf(fileSize))
                .body(data);
    }

    @GetMapping("/{token}/api/track/delete")
    public ResponseEntity<?> deleteSong(@PathVariable("token") String token, @RequestParam(name = "id") long songId)
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

        boolean deleted = songService.deleteSong(songId);
        if(deleted == false) return new ResponseEntity<>("FALSE", HttpStatus.OK);
        return new ResponseEntity<>("TRUE",HttpStatus.OK);
    }
    @PostMapping("/{token}/api/track/add")
    public ResponseEntity<?> createSong(@PathVariable("token") String token,
                                        @RequestParam("file") MultipartFile file,
                                        @RequestParam("album_id") long album_id,
                                        @RequestParam("track_name") String track_name) throws IOException

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
        int numOfSong = songService.findSongInAlbum(album_id).size();
        InputStream inputStream = file.getInputStream();
        String path = defaultSongDir + "\\"+Long.toString(artist_id)+"\\"+Long.toString(album_id);
        OutputStream outputStream = new FileOutputStream(new File(path+"\\"+Integer.toString(numOfSong+1)+".mp3"));
        byte[] buffer = new byte[1024];
        int len;
        while((len = inputStream.read(buffer))>0)
        {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        song newSong = new song();
        newSong.setActive(true);
        newSong.setTrack_name(track_name);
        newSong.setTrack_num(numOfSong+1);
        newSong.setAlbum_id(album_id);
        newSong = songService.save(newSong);

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body(newSong);
    }
}
