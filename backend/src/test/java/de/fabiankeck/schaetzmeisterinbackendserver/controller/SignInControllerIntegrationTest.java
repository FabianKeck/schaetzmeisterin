package de.fabiankeck.schaetzmeisterinbackendserver.controller;

import de.fabiankeck.schaetzmeisterinbackendserver.game.GameService;

import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.Date;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = "jwt.secretkey=somesecretkey")
class SignInControllerIntegrationTest {

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


    @Test
    @DisplayName("Get with param should return token")
    public void getTest(){
        //given
        String username= "Jane";
        String url = "http://localhost:"+port+"/signin/?username="+username;
        //when
        when(idUtils.createId()).thenReturn("id");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);


         //then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        String token = response.getBody();
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();


        assertThat(claims.getSubject(), is(username));
        assertThat(claims.get("playerId", String.class), is("id"));
        assertThat(claims.getExpiration().after(Date.from(Instant.now())),is(true));

    }

    @Test
    @DisplayName("Get without param should throw")
    public void getTestWithoutParam(){
        //given
        String url = "http://localhost:"+port+ "/signin/";
        //when
        when(idUtils.createId()).thenReturn("id");
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);


         //then
        assertThat(response.getStatusCode(),is(HttpStatus.BAD_REQUEST));


    }





}