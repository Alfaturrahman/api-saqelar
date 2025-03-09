package com.example.tester.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "ThisIsAReallyStrongKeyThatIsLongEnough123456"; // Min 32 chars
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 Jam (dalam milidetik)

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); // Tidak perlu Base64 decode
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), Jwts.SIG.HS256) // Gunakan SIG.HS256 di JJWT 0.12.0
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // JJWT 0.12.0 pakai verifyWith()
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }
}
