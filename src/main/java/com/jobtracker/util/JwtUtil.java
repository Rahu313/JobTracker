package com.jobtracker.util;

import java.util.Date;

import com.jobtracker.model.User;

import io.jsonwebtoken.*;

public class JwtUtil {

    private static final String SECRET_KEY = "Rahu86@!=";
    private static final long EXPIRATION_TIME = 86400000; // 1 day

    public static String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}

