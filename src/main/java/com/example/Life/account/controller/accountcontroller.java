package com.example.Life.account.controller;

import com.example.Life.JWT;
import com.example.Life.LifeApplication;
import com.example.Life.account.entity.account;
import com.example.Life.account.model.*;
import com.example.Life.account.service.accountservice;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class accountcontroller
{
    @Autowired
    private accountservice accountService;

    @PostMapping(path = "/api/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> authenticate(@RequestBody Map<String, String> body)
    {
        System.out.println(LifeApplication.POST+" /api/authenticate " + body.toString());
        if(body.get("token")!=null)
        {
            Claims claims =  JWT.decodeJWT(body.get("token"));
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
            long account_id  = Long.parseLong(subject);
            account current = accountService.findAccount(account_id);
            Map<String, String> response = new HashMap<>();
            response.put("token", body.get("token"));
            response.put("display_name", current.getDisplay_name());
            response.put("role", Long.toString(current.getRole()));
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body(response);
        }
        else
        {
            account authentication = accountService.Authenticate(body.get("email"), body.get("password"));
            if (authentication == null) {
                return ResponseEntity.status(HttpStatus.OK)
                        .header("Content-Type", "application/json")
                        .body("{\"status\":\"Wrong email or password\"}");
            }
            Map<String, String> response = new HashMap<>();
            response.put("token", JWT.createJWT(Long.toString(authentication.getId())));
            response.put("display_name", authentication.getDisplay_name());
            response.put("role", Long.toString(authentication.getRole()));
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type", "application/json")
                    .body(response);
        }
    }

    @PostMapping(path = "/api/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody registermodel registerModel)
    {
        System.out.println(LifeApplication.POST+" /api/register "+ registerModel.toString());
        if(!accountService.Register(registerModel.getEmail(),registerModel.getPassword(),registerModel.getDisplay_name(),registerModel.getRole()))
        {
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Email has been used\"}");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }

    @PostMapping("/api/users/{id}/avatar")
    public ResponseEntity<?> uploadCover(@RequestParam(name = "token") String token, @RequestParam("file") MultipartFile file,
                                      @PathVariable("id") long user_id)
    {
        System.out.println(LifeApplication.POST+" /api/users/"+user_id+"/avatar " +token);
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
            InputStream inputStream = file.getInputStream();
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            String _path = LifeApplication.defaultDataDir+"\\"+"avatars";
            File curFolder = new File(_path);
            if(curFolder.exists() == false)
            {
                curFolder.mkdirs();
            }
            String path = LifeApplication.defaultDataDir+"\\"+"avatars"+"\\"+user_id+".jpeg";
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
    @GetMapping("/api/artists")
    public ResponseEntity<?> getAllArtists(@RequestParam(name = "token") String token, @RequestParam(name = "index", defaultValue = "1" , required = false) int index)
    {
        System.out.println(LifeApplication.GET + " /api/artists " + token);
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
        int toIndex = Math.min(listArtists.size()-1,index*perPage-1)+1;
        if(fromIndex>=toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body("[]");

        List<?> output = listArtists.subList(fromIndex, toIndex);
        return new ResponseEntity<>( output.size() == 0 ? "[]" : output, HttpStatus.OK);
    }
    @GetMapping("/api/artists/{id}")
    public ResponseEntity<?> getArtist(@RequestParam(name = "token") String token, @PathVariable("id") long artist_id)
    {
        System.out.println(LifeApplication.GET+" /api/artists/"+artist_id+" "+token);
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

        return new ResponseEntity<>(accountService.getArtist(artist_id) == null ? "[]" : accountService.getArtist(artist_id), HttpStatus.OK);
    }

    @GetMapping("/api/users")
    public ResponseEntity<?> getAllUsers(@RequestParam(name = "token") String token, @RequestParam(name = "index", defaultValue = "1", required = false) int index)
    {
        System.out.println(LifeApplication.GET+" /api/users "+token);
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
        int toIndex = Math.min(listUsers.size()-1,index*perPage-1)+1;
        if(fromIndex>=toIndex) return ResponseEntity.status(HttpStatus.OK).header("Content-Type","application/json").body("[]");
        List<?> output = listUsers.subList(fromIndex, toIndex);
        return new ResponseEntity<>(output.size() ==0 ? "[]" : output, HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<?> getUser(@RequestParam(name = "token") String token, @PathVariable("id") long user_id)
    {
        System.out.println(LifeApplication.GET+" /api/users/"+user_id+" "+token);
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

        return new ResponseEntity<>(accountService.getUser(user_id) == null ? "[]" : accountService.getUser(user_id), HttpStatus.OK);
    }

    @GetMapping("/api/users/{id}/avatar")
    public ResponseEntity<?> getUserCover(@RequestParam(name = "token") String token, @PathVariable("id") long user_id)
    {
        System.out.println(LifeApplication.GET + " /api/users/"+user_id+"/avatar "+token);
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
        String path = LifeApplication.defaultDataDir + "\\" +"avatars"+"\\"+user_id+".jpeg";
        File file = new File(path);
        BufferedImage image ;
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
    @PutMapping("/api/users/{id}")
    public ResponseEntity<?> editUser(
            @RequestParam(name = "token") String token, @PathVariable("id") long user_id, @RequestBody Map<String, String> body)
    {
        System.out.println(LifeApplication.PUT+"/api/users/"+user_id+" "+body.toString());
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
        if(currentAccount == null)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong user_id\"}");

        if(body.get("active")!=null)
            currentAccount.setActive(Boolean.parseBoolean(body.get("active")));
        if(body.get("display_name")!=null)
            currentAccount.setDisplay_name(body.get("display_name"));
        if(body.get("password")!=null)
            currentAccount.setPassword(body.get("password"));
        if(body.get("role")!=null)
            currentAccount.setRole(Integer.parseInt(body.get("role")));
        accountService.save(currentAccount);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }


    @PutMapping("/api/artists/{id}")
    public ResponseEntity<?> editArtist(
            @RequestParam(name = "token") String token, @PathVariable("id") long artist_id, @RequestBody Map<String, String> body)
    {
        System.out.println(LifeApplication.PUT+" /api/artists/"+artist_id+" "+body.toString());
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
        if(body.get("active")!=null)
            currentAccount.setActive(Boolean.parseBoolean(body.get("active")));
        if(body.get("display_name")!=null)
            currentAccount.setDisplay_name(body.get("display_name"));
        if(body.get("password")!=null)
            currentAccount.setPassword(body.get("password"));
        if(body.get("role")!=null)
            currentAccount.setRole(Integer.parseInt(body.get("role")));
        accountService.save(currentAccount);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }

    @PostMapping("/api/users")
    public ResponseEntity<?> createAccount(@RequestParam(name = "token") String token,
                                           @RequestBody Map<String, String> body)
    {
        System.out.println(LifeApplication.POST+" "+"/api/users/"+1 + " "+token);
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
        long id = Long.parseLong(subject);
        if(id!=1)
        {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong token\"}");
        }

        account newAccount = new account();
        newAccount.setActive(true);
        if(body.get("active") !=null)
            newAccount.setActive(Boolean.parseBoolean(body.get("active")));
        if(body.get("display_name")!=null)
            newAccount.setDisplay_name(body.get("display_name"));
        if(body.get("email")!=null)
            newAccount.setEmail(body.get("email"));
        if(body.get("password")!=null)
            newAccount.setPassword(body.get("password"));
        if(body.get("role")!=null)
            newAccount.setRole(Integer.parseInt(body.get("role")));
        accountService.save(newAccount);
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }

    @GetMapping("/api/users/{id}/artists/favorite")
    public ResponseEntity<?> getAllFavArtist(@RequestParam("token") String token,
                                             @PathVariable("id") long user_id)
    {
        System.out.println(LifeApplication.GET+" "+"/api/users/"+user_id + "/artists/favorite "+token);
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
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body(accountService.getAllFavArtist(user_id));
    }
    @PostMapping("/api/users/{id}/artists/favorite")
    public ResponseEntity<?> addNewFavArtist(@RequestParam("token") String token,
                                             @PathVariable("id") long user_id,
                                             @RequestBody Map<String, String> body)
    {
        System.out.println(LifeApplication.POST+" "+"/api/users/"+user_id + "/artists/favorite "+token);
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
        long artist_id = -1;
        if(body.get("artist_id") != null) artist_id = Long.parseLong(body.get("artist_id"));
        if(artist_id == -1)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong artist_id\"}");
        accountService.addNewFavArtist(user_id, artist_id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }
    @DeleteMapping("/api/users/{id}/artists/favorite")
    public ResponseEntity<?> deleteFavArtist(@RequestParam("token") String token,
                                             @PathVariable("id") long user_id,
                                             @RequestBody Map<String, String> body)
    {
        System.out.println(LifeApplication.DELETE+" "+"/api/users/"+user_id + "/artists/favorite "+token);
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
        long artist_id = -1;
        if(body.get("artist_id") != null) artist_id = Long.parseLong(body.get("artist_id"));
        if(artist_id == -1)
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("Content-Type","application/json")
                    .body("{\"status\":\"Wrong artist_id\"}");
        accountService.deleteFavArtist(user_id, artist_id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header("Content-Type","application/json")
                .body("{\"status\":\"success\"}");
    }
}


