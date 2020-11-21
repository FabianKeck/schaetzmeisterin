package de.fabiankeck.schaetzmeisterinbackendserver.security;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

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
    @BeforeEach
    void clear(){
        gameService.clearGames();
    }




    @Test
    void PostWithValidTokenShouldReturn200Ok(){
        String token = Jwts.builder()
                .setSubject("john")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(1L))))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        String url = "http://localhost:"+port+"/api/game/signin";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> request= new HttpEntity<>("",headers );

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST , request,String.class);

        assertThat(responseEntity.getStatusCode(),is(HttpStatus.OK)) ;

    }

    @Test
    void PostWithExpiredTokenShouldReturn403Forbidden(){
        String token = Jwts.builder()
                .setSubject("john")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().minus(Duration.ofHours(1))))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        String url = "http://localhost:"+port+"/api/game/signin";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> request= new HttpEntity<>("",headers );

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST , request,String.class);

        assertThat(responseEntity.getStatusCode(),is(HttpStatus.FORBIDDEN)) ;
    }
    @Test
    void PostWithTokenwithWrongKeyShouldReturn403Forbidden(){
        String token = Jwts.builder()
                .setSubject("john")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(1))))
                .signWith(SignatureAlgorithm.HS256,"wrongKey")
                .compact();
        String url = "http://localhost:"+port+"/api/game/signin";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> request= new HttpEntity<>("",headers );

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST , request,String.class);

        assertThat(responseEntity.getStatusCode(),is(HttpStatus.FORBIDDEN)) ;
    }

}