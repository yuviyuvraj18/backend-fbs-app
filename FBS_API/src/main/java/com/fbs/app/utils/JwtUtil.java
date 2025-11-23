package com.fbs.app.utils;

import com.fbs.app.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration-ms}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-expiration-ms}")
    private long refreshTokenExpirationMs;

    @Autowired
    private UserRepository userRepository;

    private String email;

    // -------------------------------
    // 1) Create Signing Key
    // -------------------------------
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes); // must be 256-bit or higher
    }

    // -------------------------------
    // 2) Generate Access Token
    // -------------------------------
    public String generateAccessToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles) // store roles
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // -------------------------------
    // 3) Generate Refresh Token
    // -------------------------------
    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // -------------------------------
    // 4) Extract Email From Token
    // -------------------------------
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // -------------------------------
    // 5) Get Expiry Date of Token
    // -------------------------------
    public Date extractExpiration(String token) {

        return extractAllClaims(token).getExpiration();
    }

    // -------------------------------
    // 6) Validate Token
    // -------------------------------
    public boolean isTokenValid(String token) {
        final String extractedEmail = extractEmail(token);
        return (userRepository.findByEmail(extractedEmail).isPresent() && !isTokenExpired(token));
    }


    // -------------------------------
    // 7) Check if Token Expired
    // -------------------------------
    public boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());

    }

    // -------------------------------
    // 8) Get Claims Safely
    // -------------------------------
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}




//package com.fbs.app.utils;
//
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//    @Value("${jwt.secret}")
//
//    private String secret;
//
//
//    private Key getSigningKey() {
//        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public String generateToken(String email) {
//        return Jwts.builder()
//                .setSubject(email)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60))
//                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public String extractEmail(String token) {
//        return Jwts.parser().setSigningKey(secret)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//    }
//}
