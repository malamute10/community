package com.personal.community.config.jwt;


import com.personal.community.config.exception.CommunityException;
import com.personal.community.config.exception.ExceptionEnum;
import com.personal.community.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.sql.Date;
import java.time.LocalDate;
import javax.security.auth.kerberos.EncryptionKey;
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

    public String generateAccessToken(User user) {
        return this.generateToken(user, ACCESS_TOKEN_DURATION);
    }
    private String generateToken(User user, Long expirationTime) {
        Claims claims = Jwts.claims().setSubject(user.getEmail());
        claims.put("ROLE", user.getUserRole());
        claims.setIssuedAt(Date.valueOf(LocalDate.now()));


        return Jwts.builder().setClaims(claims).setIssuedAt(claims.getIssuedAt())
                .setExpiration(new Date(LocalDate.now().toEpochDay() + expirationTime))
                .signWith(this.getKey(), SignatureAlgorithm.HS512).compact();
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
        return new EncryptionKey(SECRET.getBytes(), 0);
    }
}
