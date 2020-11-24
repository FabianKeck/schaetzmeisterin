package de.fabiankeck.schaetzmeisterinbackendserver.controller;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=somesecretkey")
class GameControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Value("${jwt.secretkey}")
    private String secretKey;

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    IdUtils idUtils;

    @Autowired
    GameService gameService;
    @BeforeEach
    void clear(){
        gameService.clearGames();
    }

    @Test
    @DisplayName("Post on /signin without gameId should return new Game")
    void signInNewTest(){
        //given
        String token = Jwts.builder()
                .setSubject("John")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(1L))))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();

        HttpHeaders headers= new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> request= new HttpEntity<>(null,headers);
        String url = "http://localhost:"+port+"/api/game/signin";
        //when
        when(idUtils.createId()).thenReturn("id");
        ResponseEntity<Game> response = restTemplate.exchange(url, HttpMethod.POST, request, Game.class);

        //then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(response.getBody(),is(new Game("id",List.of(new Player("John")))));
    }

    @Test
    @DisplayName("Post on /signin with gameId should return new Game")
    void signInExistingTest(){
        //given
        String token1 = Jwts.builder()
                .setSubject("John")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(1L))))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
        String token2 = Jwts.builder()
                .setSubject("Doe")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(Duration.ofHours(1L))))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
            //createGame withFirst user
        HttpHeaders createHeaders = new HttpHeaders();
        createHeaders.setBearerAuth(token1);
        HttpEntity<Void> createRequest= new HttpEntity<>(null,createHeaders);
        String createUrl = "http://localhost:"+port+"/api/game/signin";
        when(idUtils.createId()).thenReturn("id2");
        restTemplate.exchange(createUrl, HttpMethod.POST, createRequest, Game.class);

        //when
        HttpHeaders headers= new HttpHeaders();
        headers.setBearerAuth(token2);
        HttpEntity<Void> request= new HttpEntity<>(null,headers);
        String url = "http://localhost:"+port+"/api/game/signin/id2";
        ResponseEntity<Game> response = restTemplate.exchange(url, HttpMethod.POST, request, Game.class);



        //then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(response.getBody().getId(),is("id2"));
        assertThat(response.getBody().getPlayers(),containsInAnyOrder(new Player("John"),
                new Player("Doe")));
    }
}