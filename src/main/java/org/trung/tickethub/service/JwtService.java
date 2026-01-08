package org.trung.tickethub.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.trung.tickethub.entity.Role;
import org.trung.tickethub.entity.User;
import org.trung.tickethub.exception.AppException;
import org.trung.tickethub.constant.TokenType;


import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.function.Function;

import static org.trung.tickethub.exception.ErrorCode.INVALID_TOKEN;
import static org.trung.tickethub.constant.TokenType.ACCESS_TOKEN;
import static org.trung.tickethub.constant.TokenType.REFRESH_TOKEN;


@Service
@Slf4j
public class JwtService {
    @Value("${security.jwt.access-duration}")
    private long accessDuration;

    @Value("${security.jwt.refresh-duration}")
    private long refreshDuration;

    @Value("${security.jwt.access-key}")
    private String accessKey;

    @Value("${security.jwt.refresh-key}")
    private String refreshKey;

    public String generateToken(User user) {
        return generateToken(Map.of("userId", user.getAuthorities()), user);
    }

    public String generateRefreshToken(User user) {
        return generateRefreshToken(new HashMap<>(), user);
    }

    public String extractUsername(String token, TokenType type) {
        return extractClaim(token, type, Claims::getSubject);
    }

    public boolean isValid(String token, TokenType type, UserDetails user) {
        final String username = extractUsername(token, type);
        return (username.equals(user.getUsername()) && !isTokenExpired(token, type));
    }

    private String generateToken(Map<String, Object> claims, User user) {
        List<String> roleNames = getRoleNames(user.getRoles());
        String id = user.getId();
        return Jwts.builder()
                .claims(claims)
                .claim("scope", roleNames)
                .claim("id", id)
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessDuration * 1000 * 60 * 60 * 24))
                .signWith(getKey(ACCESS_TOKEN))
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, User user) {
        String id = user.getId();
        return Jwts.builder()
                .claims(claims)
                .subject(user.getUsername())
                .claim("id", id)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + refreshDuration * 1000 * 60 * 60 * 24))
                .signWith(getKey(REFRESH_TOKEN))
                .compact();
    }

    public String extractUserIdFromAccessToken(String token) {
        return extractClaim(token, ACCESS_TOKEN, claims -> claims.get("id", String.class));
    }

    private Key getKey(TokenType type) {
        if (ACCESS_TOKEN.equals(type))
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessKey));
        else
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshKey));
    }

    private <T> T extractClaim(String token, TokenType type, Function<Claims, T> claimResolver) {
        final Claims claims = extraAllClaim(token, type);
        return claimResolver.apply(claims);
    }

    private Claims extraAllClaim(String token, TokenType type) {
        try {
            return Jwts.parser().verifyWith((SecretKey) getKey(type)).build().parseSignedClaims(token).getPayload();
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            throw new AppException(INVALID_TOKEN);
        }
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration);
    }

    public List<String> getRoleNames(Set<Role> roles) {
        return roles.stream()
                .map(Role::getName)
                .toList();
    }
}
