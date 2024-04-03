package com.osbiju.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.PublicKey;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "etfpQRhfG1cRtvW9kIQlS07m2nd5CkRJi4ZGULfeFsnSZsgxZIiaqq1i1hA7J4WT";

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject); //subject should be de email of the user
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return  claimResolver.apply(claims);
    }

    //method to extract one single claim

    //aixo funciona amb la versio 0.12.5 de Jwt
//    private Claims extactAllClaims(String token){
//        return Jwts
//        .parser()
//        .verifyWith(getSinginKey())
//        .build()
//        .parseSignedClaims(token)
//        .getPayload();
//    }
//
//    @Value("${token.singing.key}")
//    private  String SECRET_KEY;

//    private SecretKey getSinginKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }

    //amb la versio 0.11.5 de Jwt com al video
    private Claims extractAllClaims(String token){
       return Jwts
               .parserBuilder()
               .setSigningKey(getSignInKey()) //signInKey is a secret key that is used to digitally sign the Jwt, is used to create the signature part of the jwt which is use to verify that the sender of jwt is who is claim to be and ensure that the message is not change along the way
               .build()
               .parseClaimsJws(token)
               .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);

    }


}
