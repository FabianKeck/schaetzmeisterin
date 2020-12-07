package de.fabiankeck.schaetzmeisterinbackendserver.controller;

import de.fabiankeck.schaetzmeisterinbackendserver.dao.GameDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.BetDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.model.SmUser;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.notNull;
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
        assertThat(Objects.requireNonNull(response.getBody()).getId(),is(gameId));


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
        HttpEntity<Object> request = getValidAuthenticationEntity(null, username2, playerId2);
        String url = "http://localhost:"+port+"/api/game/signin/"+gameId;
        ResponseEntity<Game> response = restTemplate.exchange(url, HttpMethod.POST, request, Game.class);

        //then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).getId(),is(gameId));
        assertThat(response.getBody().getPlayers().stream().map(Player::getId).collect(Collectors.toList()), containsInAnyOrder(playerId1, playerId2));
    }
    @Test
    @DisplayName("post on startGame should return updated Game")
    public void startTest(){
        //given
        String gameId= "gameId";
        String username1= "John";
        String playerId1 = "123";

        HttpEntity<Object> signInRequest = getValidAuthenticationEntity(null,username1,playerId1);
        when(idUtils.createId()).thenReturn(gameId);
        String signInUrl = "http://localhost:"+port+"/api/game/signin";
        restTemplate.exchange(signInUrl, HttpMethod.POST, signInRequest, Game.class);
        //when
        String startUrl = "http://localhost:"+port+"/api/game/startgame/"+gameId;
        ResponseEntity<Game> response = restTemplate.exchange(startUrl, HttpMethod.POST, signInRequest, Game.class);

        //then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).isStarted(),is(true));
    }
     @Test
    @DisplayName("betRequest on startedGame with BetValue in correct range should return updated Game")
    public void betTest(){
        //given
        String gameId= "gameId";
        String username1= "John";
        String playerId1 = "123";
        String user1Token = login(username1,playerId1);
        int betValue =2;

        //sign in first user
         HttpHeaders firstUserAuthHeaders = new HttpHeaders();
         firstUserAuthHeaders.setBearerAuth(user1Token);
        HttpEntity<Object> firstSignInRequest = new HttpEntity<>(null,firstUserAuthHeaders);
        when(idUtils.createId()).thenReturn(gameId);
        String signInUrl = "http://localhost:"+port+"/api/game/signin";
        restTemplate.exchange(signInUrl, HttpMethod.POST, firstSignInRequest, Game.class);


        //signIn second user
         String username2= "Doe";
         String playerId2 = "456";
         HttpEntity<Object> secondSignInRequest = getValidAuthenticationEntity(null,username2,playerId2);
         String signInUrlId = "http://localhost:"+port+"/api/game/signin/"+gameId;
         restTemplate.exchange(signInUrlId, HttpMethod.POST, secondSignInRequest, Game.class);


         //startGame
         String startUrl = "http://localhost:"+port+"/api/game/startgame/"+gameId;
         restTemplate.exchange(startUrl, HttpMethod.POST, firstSignInRequest, Game.class);

         //when
         HttpEntity<BetDto> betEntity = new HttpEntity<>(new BetDto(betValue), firstUserAuthHeaders);
        String betUrl = "http://localhost:"+port+"/api/game/bet/"+gameId;
         ResponseEntity<Game> response = restTemplate.exchange(betUrl, HttpMethod.POST, betEntity, Game.class);



         //then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response.getBody()).getBetSession().getActivePlayerIndex(),is(1));
        assertThat(response.getBody().getBetSession().getPlayers().get(0).getCurrentBet(),is(betValue));
    }
    
    


}