package com.sandeep.authentication.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// import javax.annotation.PostConstruct; depricated  not sppported in bIn Spring Boot 3.x + JJWT 0.12.x
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;

import javax.crypto.SecretKey;



@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}") // in seconds
    private long jwtExpiration;

    private  SecretKey secretKey;


    @PostConstruct
    public void init() {
        // Generate signing key from secret
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }


 public Claims getClaims(String token) {

         return    Jwts.parser()
                   .verifyWith(this.secretKey)          // replaces setSigningKey(), safer type
                   .build()
                   .parseSignedClaims(token)   // updated parsing method
                   .getPayload();
                   // below are are depricated in jjwt version that we are using

        // return Jwts.parserBuilder()
        //            .setSigningKey(this.secretKey)
        //            .build()
        //            .parseClaimsJws(token)
        //            .getBody();

        //  return Jwts.parser()
        //            .setSigningKey(secret.getBytes()) // directly use secret
        //            .parseClaimsJws(token)
        //            .getBody();
    }
    /**
     * Generate token with claims
     */
    // public String generateToken(Map<String, Object> claims, String subject) {
    //     return Jwts.builder()
    //             // .claims(claims) // put custom claims like role, subscription, etc.
    //             .addClaims(claims)
    //             .subject(subject)
    //             .issuedAt(new Date(System.currentTimeMillis()))
    //             .expiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
    //             .signWith(secretKey, Jwts.SIG.HS256) // ✅ new way in 0.12.x
    //             .compact();
    // }

public String extractRole(String token) {
    return getClaims(token).get("role", String.class);
}
    public String generateToken(Map<String, Object> claims, String subject) {
    return Jwts.builder()
            .addClaims(claims) // custom claims
            .subject(subject)  // sub claim
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
            .signWith(secretKey, Jwts.SIG.HS256) // ✅ requires javax.crypto.SecretKey
            .compact();
}


// chaininh , user ne older version user kr rah eh but idr tech u[date ho gaya to usko to puran wala style and yaha se hum naya wlaa]
public String generateToken(String subject) {
    return generateToken(new HashMap<>(), subject);
}

    /**
     * Generate simple token with just subject (no claims)
     */
    // public String generateToken(String subject) {
    //     return Jwts.builder()
    //             .subject(subject)
    //             .issuedAt(new Date(System.currentTimeMillis()))
    //             .expiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
    //             .signWith(secretKey, Jwts.SIG.HS256)
    //             .compact();
    // }

    /**
     * Extract all claims
     */
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // ✅ 0.12.x replaces parserBuilder().setSigningKey()
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Extract username (subject)
     */
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    /**
     * Validate token
     */
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }


  // Validate token without UserDetails (optional, simple check)
    public boolean validateToken(String token) {
        return !isTokenExpired(token);
    }
    /**
     * Check expiration
     */
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }
}
