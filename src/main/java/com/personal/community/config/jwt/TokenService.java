package com.personal.community.config.jwt;


import com.personal.community.common.CommunityEnum;
import com.personal.community.config.exception.CommunityException;
import com.personal.community.config.exception.ExceptionEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.sql.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TokenService {

    @Value("${jwt.access-token}")
    private Long ACCESS_TOKEN_DURATION;

    @Value("${jwt.secret}")
    private String SECRET;

    public String generateAccessToken(String email, CommunityEnum.UserRole userRole) {
        return this.generateToken(email, userRole, ACCESS_TOKEN_DURATION);
    }
    private String generateToken(String email, CommunityEnum.UserRole userRole, Long expirationTime) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("ROLE", userRole);
        claims.setIssuedAt(new Date(System.currentTimeMillis()));

        return Jwts.builder().setClaims(claims).setIssuedAt(claims.getIssuedAt())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(this.getKey()).compact();
    }

    public boolean verifyToken(String token) {
        try {
            return this.isExpired(token);
        } catch (ExpiredJwtException e) {
            throw CommunityException.of(ExceptionEnum.EXPIRED_TOKEN);
        }
    }

    public String getUsername(String token){
        return this.getClaims(token).getSubject();
    }

    public String getUserRole(String token){
        return (String) this.getClaims(token).get("ROLE");
    }

    private boolean isExpired(String token) {
        return this.getClaims(token).getExpiration().after(new Date(System.currentTimeMillis()));
    }

    private Claims getClaims(String token) {
        return (Claims) Jwts.parserBuilder().setSigningKey(this.getKey()).build().parse(token).getBody();
    }

    private Key getKey(){
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }
}
