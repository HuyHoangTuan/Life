package com.example.Life.song.controller;

import com.example.Life.JWT;
import com.example.Life.song.entity.song;
import com.example.Life.song.model.songmodel;
import com.example.Life.song.service.songservice;

import io.jsonwebtoken.Claims;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileFormat;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

    @GetMapping("/{token}/api/track/detail/duration")
    public ResponseEntity<?> getSongDuration(@PathVariable("token") String token, @RequestParam(name = "id") long songId) throws IOException, UnsupportedAudioFileException
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
        songmodel currentSong = songService.getSong(songId);
        if(currentSong == null) return new ResponseEntity<>("",HttpStatus.OK);
        String songPath = "\\"
                +Long.toString(currentSong.getArtist_id())+"\\"
                +Long.toString(currentSong.getAlbum_id())+"\\"
                +Long.toString(currentSong.getTrack_num())+".mp3";
        File file = new File(defaultSongDir+songPath);

        AudioFileFormat audioFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
        Map properties = audioFileFormat.properties();
        Long duration = (Long) properties.get("duration");
        return new ResponseEntity<>(duration*0.000001,HttpStatus.OK);

    }
    @GetMapping("/{token}/api/track/detail")
    public ResponseEntity<?> getSong(@PathVariable("token") String token, @RequestParam(name ="id") long sondId)
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

        songmodel currentSong = songService.getSong(sondId);
        if(currentSong == null) return new ResponseEntity<>("", HttpStatus.OK);
        return new ResponseEntity<>(currentSong, HttpStatus.OK);
    }

    @GetMapping("/{token}/api/track/play")
    public ResponseEntity<?> streamingAudio(@PathVariable("token") String token,@RequestParam(name = "id") long songId) throws IOException
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

        songmodel currentSong = songService.getSong(songId);
        if(currentSong == null) return new ResponseEntity<>("FALSE", HttpStatus.OK);
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

}
