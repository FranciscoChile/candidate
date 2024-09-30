package com.pancho.demo.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;


@Service
public class TokenProvider {

    @Value("${security.jwt.secret-key}")
    private String secret;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.expiry-time-in-seconds}")
    private Long expiryTimeInSeconds;

    @Autowired
    AuthenticationManager authenticationManager;


    public String createToken(Authentication authentication) {
        String username = authentication.getName();
        return Jwts.
                builder()
                .issuer(issuer)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (expiryTimeInSeconds * 1000)))
                .claim("username", username)
                .claim("authorities", authentication.getAuthorities())
                .signWith(getSecretKey()).compact();
    }

    public Authentication setAuthentication(String token) {
        Claims payload = parseClaimsFromToken(token);
        String username = payload.getSubject();
        List<LinkedHashMap<String, String>> authorities = payload.get("authorities", ArrayList.class);
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream().map(
                a -> new SimpleGrantedAuthority(a.get("authority"))
        ).toList();
        return new UsernamePasswordAuthenticationToken(username, "", grantedAuthorities);
    }

    public boolean validateToken(String token) {
        try {
            parseClaimsFromToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims parseClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build().parseSignedClaims(token).getPayload();
    }
}