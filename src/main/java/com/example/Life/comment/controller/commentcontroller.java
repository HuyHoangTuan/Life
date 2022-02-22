package com.example.Life.comment.controller;

import com.example.Life.JWT;
import com.example.Life.LifeApplication;
import com.example.Life.comment.entity.comment;
import com.example.Life.comment.service.commentservice;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class commentcontroller
{
    @Autowired
    private commentservice commentService;

    @GetMapping("/api/comments")
    public ResponseEntity<?> getCommentFrom(@RequestParam(name = "token") String token,
                                            @RequestParam(name = "uid", required = false, defaultValue = "-1") int user_id,
                                            @RequestParam(name = "album", required = false, defaultValue = "-1") int album_id,
                                            @RequestParam(name = "playlist", required = false, defaultValue = "-1") int playlist_id,
                                            @RequestParam(name = "index", required = false, defaultValue = "1") int index)
    {
        System.out.println(LifeApplication.GET+" /api/comments "+user_id+", "+album_id+", "+playlist_id+" "+token);
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
        if(user_id == -1 && album_id == -1 && playlist_id == -1)
        {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Missing params\"}");
        }
        if(user_id ==-1)
        {
            if(album_id != -1)
            {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Content-Type","application/json")
                        .body(commentService.getAllCommentOfAlbum(album_id));
            }
            else
            {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Content-Type","application/json")
                        .body(commentService.getAllCommentOfPlaylist(playlist_id));
            }
        }
        else
        {
            if(album_id == -1 && playlist_id == -1)
            {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Content-Type","application/json")
                        .body(commentService.getAllCommentOfUser(user_id));
            }
            if(album_id != -1)
            {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Content-Type","application/json")
                        .body(commentService.getAllCommentOfUserInAlbum(user_id, album_id));
            }
            else
            {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .header("Content-Type","application/json")
                        .body(commentService.getALlCommentOfUserInPlaylist(user_id, album_id));
            }
        }
    }
    @PostMapping("/api/comments")
    public ResponseEntity<?> addAComment(@RequestParam(name = "token") String token,
                                            @RequestParam(name = "uid", required = false, defaultValue = "-1") int user_id,
                                            @RequestParam(name = "album", required = false, defaultValue = "-1") int album_id,
                                            @RequestParam(name = "playlist", required = false, defaultValue = "-1") int playlist_id,
                                            @RequestBody Map<String, String> body)
    {
        System.out.println(LifeApplication.POST+" /api/comments "+user_id+", "+album_id+", "+playlist_id+" "+token);
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
        if(user_id == -1 || (album_id== -1 && playlist_id == -1))
        {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Missing params\"}");
        }

        String content = body.get("content");
        Date current = new Date(System.currentTimeMillis());
        if(album_id != -1)
        {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(commentService.addCommentToAlbum(user_id, album_id, content, current));
        }
        else
        {
            commentService.addCommentToPlaylist(user_id, playlist_id, content, current);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(commentService.addCommentToPlaylist(user_id, playlist_id, content, current));
        }
    }
    @GetMapping("/api/albums/{id}/comments")
    public ResponseEntity<?> getAllCommentOfAlbum(@RequestParam(name = "token") String token, @PathVariable("id") int album_id,
                                                  @RequestParam(name = "index", defaultValue = "1", required = false) int index)
    {
        System.out.println(LifeApplication.GET+" /api/albums/"+album_id+"/comments "+token);
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
        List<?> listComments = commentService.getAllCommentOfAlbum(album_id);
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(listComments.size()-1,index*perPage-1)+1;
        if(fromIndex>=toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(listComments.subList(fromIndex, toIndex));
    }
    @GetMapping("/api/playlists/{id}/comments")
    public ResponseEntity<?> getAllCommentOfPlaylist(@RequestParam(name = "token") String token, @PathVariable("id") int playlist_id,
                                                  @RequestParam(name = "index", defaultValue = "1", required = false) int index)
    {
        System.out.println(LifeApplication.GET+" /api/playlists/"+playlist_id+"/comments "+token);
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
        List<?> listComments = commentService.getAllCommentOfPlaylist(playlist_id);
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(listComments.size()-1,index*perPage-1)+1;
        if(fromIndex>=toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(listComments.subList(fromIndex, toIndex));
    }
    @GetMapping("/api/users/{id}/comments")
    public ResponseEntity<?> getAllCommentOfUser(@RequestParam(name = "token") String token, @PathVariable("id") int user_id,
                                                     @RequestParam(name = "index", defaultValue = "1", required = false) int index)
    {
        System.out.println(LifeApplication.GET+" /api/users/"+user_id+"/comments "+token);
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
        List<?> listComments = commentService.getAllCommentOfUser(user_id);
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(listComments.size()-1,index*perPage-1)+1;
        if(fromIndex>=toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(listComments.subList(fromIndex, toIndex));
    }
    @GetMapping("/api/users/{id}/albums/{album_id}/comments")
    public ResponseEntity<?> getAllCommentOfUserInAlbum(@RequestParam(name = "token") String token, @PathVariable("id") int user_id,
                                                  @PathVariable("album_id") int album_id,
                                                  @RequestParam(name = "index", defaultValue = "1", required = false) int index)
    {
        System.out.println(LifeApplication.GET+" /api/users/"+user_id+"/albums/ "+album_id+"/comments "+token);
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
        List<?> listComments = commentService.getAllCommentOfUserInAlbum(user_id, album_id);
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(listComments.size()-1,index*perPage-1)+1;
        if(fromIndex>=toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(listComments.subList(fromIndex, toIndex));
    }

    @GetMapping("/api/users/{id}/playlists/{playlist_id}/comments")
    public ResponseEntity<?> getAllCommentOfUserInPlaylist(@RequestParam(name = "token") String token, @PathVariable("id") int user_id,
                                                        @PathVariable("playlist_id") int playlist_id,
                                                        @RequestParam(name = "index", defaultValue = "1", required = false) int index)
    {
        System.out.println(LifeApplication.GET+" /api/users/"+user_id+"/playlists/ "+playlist_id+"/comments "+token);
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
        List<?> listComments = commentService.getALlCommentOfUserInPlaylist(user_id, playlist_id);
        int perPage = 20;
        int fromIndex = (index-1)*perPage;
        int toIndex = Math.min(listComments.size()-1,index*perPage-1)+1;
        if(fromIndex>=toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body(null);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", "application/json")
                .body(listComments.subList(fromIndex, toIndex));
    }
    @PostMapping("/api/users/{id}/albums/{album_id}/comments")
    public ResponseEntity<?> addACommentToAlbum(@RequestParam(name = "token") String token, @PathVariable("id") int user_id,
                                                @PathVariable("album_id") int album_id,
                                                @RequestBody Map<String, String> body)
    {
        System.out.println(LifeApplication.POST+" /api/users/"+user_id+"/albums/ "+album_id+"/comments "+token);
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
        if(body.get("content")!=null && body.get("time")!=null)
            commentService.addCommentToAlbum(user_id, album_id, body.get("content"), Date.valueOf(LocalDate.parse(body.get("time"))));

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }
    @PostMapping("/api/users/{id}/playlist/{playlist_id}/comments")
    public ResponseEntity<?> addACommentToPlaylist(@RequestParam(name = "token") String token, @PathVariable("id") int user_id,
                                                @PathVariable("playlist_id") int playlist_id,
                                                @RequestBody Map<String, String> body)
    {
        System.out.println(LifeApplication.POST+" /api/users/"+user_id+"/playlist/ "+playlist_id+"/comments "+token);
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
        if(body.get("content")!=null && body.get("time") != null)
            commentService.addCommentToPlaylist(user_id, playlist_id, body.get("content"), Date.valueOf(LocalDate.parse(body.get("time"))));

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }

    @GetMapping("/api/comments/{id}")
    public ResponseEntity<?> getComment(@RequestParam(name = "token") String token, @PathVariable("id") int comment_id)
    {
        System.out.println(LifeApplication.GET+" /api/comments/"+comment_id+token);
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
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body(commentService.getComment(comment_id));
    }
    @PutMapping("/api/comments/{id}")
    public ResponseEntity<?> editComment(@RequestParam(name = "token") String token, @PathVariable("id") int comment_id,
                                         @RequestBody Map<String, String> body)
    {
        System.out.println(LifeApplication.PUT+" /api/comments/"+comment_id+token);
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
        comment c = commentService.getComment(comment_id);
        if(c== null)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong comment_id\"}");
        if(body.get("content")!=null) c.setContent(body.get("content"));
        commentService.save(c);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }
    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<?> deleteComment(@RequestParam(name = "token") String token, @PathVariable("id") int comment_id
                                         )
    {
        System.out.println(LifeApplication.DELETE+" /api/comments/"+comment_id+token);
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
        comment c = commentService.getComment(comment_id);
        if(c== null)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong comment_id\"}");
        commentService.delete(c);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }
}
