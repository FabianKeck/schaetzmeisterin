package de.fabiankeck.schaetzmeisterinbackendserver.controller;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.GameDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.GameAction;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.model.SmUser;
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
import java.util.*;

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

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    IdUtils idUtils;

    @Autowired
    GameDao gameDao;

    @Autowired
    SmUserDao userDao;


    @BeforeEach
    void clear(){
        gameDao.deleteAll();
        userDao.deleteAll();
    }

    private String login (String username, String userId){
        String url = "http://localhost:"+port+"/signin/?username="+username;
        when(idUtils.createId()).thenReturn(userId);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getBody();
    }

    private <T> HttpEntity<T> getValidAuthenticationEntity(T data, String username,String userId) {
        String token = login(username,userId);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(data,headers);
    }
    @Test
    @DisplayName("Post on /signin without gameId should return new Game")
    void signInNewTest(){
        //given
        SmUser user = SmUser.builder().username("John").id("123").build();
        String gameId = "gameId";

        HttpEntity<Object> request = getValidAuthenticationEntity(null, user.getUsername(),user.getId());
        String url = "http://localhost:"+port+"/api/game/signin";
        //when
        when(idUtils.createId()).thenReturn(gameId);
        ResponseEntity<Game> response = restTemplate.exchange(url, HttpMethod.POST, request, Game.class);
        System.out.println(response.getBody());

        //then

        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(response.getBody(),is(
                Game.builder().id(gameId)
                        .playerActions(new HashMap<>(Map.of(user.getId(), GameAction.WAIT)))
                        .playerNames(new HashMap<>(Map.of(user.getId(),user.getUsername())))
                        .build()
        ));


    }

    @Test
    @DisplayName("Post on /signin with gameId should return updated Game")
    void signInExistingTest(){
        //given
        String gameId= "gameId";
        String username1= "John";
        String playerId1 = "123";
        String username2= "Doe";
        String playerId2 = "456";


        HttpEntity<Object> createRequest = getValidAuthenticationEntity(null, username1, playerId1);
        String createUrl = "http://localhost:"+port+"/api/game/signin";
        when(idUtils.createId()).thenReturn(gameId);
        restTemplate.exchange(createUrl, HttpMethod.POST, createRequest, Game.class);

        //when
        HttpHeaders headers= new HttpHeaders();
        HttpEntity<Object> request = getValidAuthenticationEntity(null, username2, playerId2);
        String url = "http://localhost:"+port+"/api/game/signin/"+gameId;
        ResponseEntity<Game> response = restTemplate.exchange(url, HttpMethod.POST, request, Game.class);

        //then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).getId(),is(gameId));
        assertThat(response.getBody().getPlayerActions().keySet(),containsInAnyOrder(playerId1, playerId2));
    }
}