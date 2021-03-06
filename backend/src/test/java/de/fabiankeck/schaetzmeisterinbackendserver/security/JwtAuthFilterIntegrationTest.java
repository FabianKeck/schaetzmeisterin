package de.fabiankeck.schaetzmeisterinbackendserver.security;

import de.fabiankeck.schaetzmeisterinbackendserver.service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.GameDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.SmUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=somesecretkey")
class JwtAuthFilterIntegrationTest {

    @LocalServerPort
    private int port;

    @Value("${jwt.secretkey}")
    private String secretKey;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    GameService gameService;
    @Autowired
    GameDao gameDao;
    @Autowired
    SmUserDao userDao;

    @BeforeEach
    void setup(){
        gameDao.deleteAll();
        userDao.deleteAll();
    }

    @Test
    void PostWithValidTokenShouldReturn200Ok(){
        String username= "john";
        userDao.save(SmUser.builder().id("123").username(username).build());
        HashMap<String, Object> claims = new HashMap<>(Map.of("playerId", "123"));
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(5))))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        String url = "http://localhost:"+port+"/api/game/signin";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<SignInUserDto> request= new HttpEntity<>(new SignInUserDto("1"),headers );

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST , request,String.class);

        assertThat(responseEntity.getStatusCode(),is(HttpStatus.OK)) ;

    }

    @Test
    void PostWithExpiredTokenShouldReturn403Forbidden(){
        String username= "john";
        HashMap<String, Object> claims = new HashMap<>(Map.of("playerId", "123"));
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().minus(Duration.ofHours(5))))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        String url = "http://localhost:"+port+"/api/game/signin";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request= new HttpEntity<>(null,headers );

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST , request,String.class);

        assertThat(responseEntity.getStatusCode(),is(HttpStatus.FORBIDDEN)) ;
    }
    @Test
    void PostWithTokenwithWrongKeyShouldReturn403Forbidden(){
        String username= "john";
        HashMap<String, Object> claims = new HashMap<>(Map.of("playerId", "123"));
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(5))))
                .signWith(SignatureAlgorithm.HS256,"wrongKey")
                .compact();
        String url = "http://localhost:"+port+"/api/game/signin";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request= new HttpEntity<>(null,headers );

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST , request,String.class);

        assertThat(responseEntity.getStatusCode(),is(HttpStatus.FORBIDDEN)) ;
    }

}