package com.example.Life;

import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JWT
{
    public static String createJWT(String object)
    {
        String SECRET_KEY = "/Fankychop123?login=Fankychop&password=Kubin123?";

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] secretkeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(secretkeyBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setSubject(object)
                .setIssuedAt(now)
                .signWith(signingKey, signatureAlgorithm);

        return builder.compact();
    }

    public static Claims decodeJWT(String jwt)
    {

        String SECRET_KEY = "/Fankychop123?login=Fankychop&password=Kubin123?";
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretkeyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(secretkeyBytes, signatureAlgorithm.getJcaName());
        Claims claims =null;
        try
        {
            claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(jwt).getBody();
        } catch (JwtException e)
        {
            return null;
        }
        return  claims;

    }
}
