package de.fabiankeck.schaetzmeisterinbackendserver.security;


import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtils {
    @Value("${jwt.secretkey}")
    private String secretKey;
    public String createToken(SignInUserDto signInUserDto, HashMap<Object, Object> objectObjectHashMap) {
        return Jwts.builder()
                .setSubject(signInUserDto.getName())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(5))))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
}
