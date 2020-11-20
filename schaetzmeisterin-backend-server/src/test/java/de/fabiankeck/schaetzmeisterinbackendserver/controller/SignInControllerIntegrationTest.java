package de.fabiankeck.schaetzmeisterinbackendserver.controller;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SignInControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;

    @MockBean
    IdUtils idUtils;

    @Autowired
    GameService gameService;

    @Test
    @DisplayName("POST with empty id-Optional should return new Game")
    public void postTest(){
        //given
        SignInUserDto signInDto = new SignInUserDto("Jörg");
        //when
        when(idUtils.createId()).thenReturn("id");
        ResponseEntity<Game> response = restTemplate.postForEntity("http://localhost:" + port + "/signin/", signInDto, Game.class);

         //then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(response.getBody(),is(new Game("id",List.of(new Player("Jörg")))));
    }

    @Test
    @DisplayName("POST with id should return updated game")
    public void postWithIdTest(){
        //given
        SignInUserDto firstUser = new SignInUserDto("Jörg");
        SignInUserDto secondUser = new SignInUserDto("Hans");

        when(idUtils.createId()).thenReturn("id");
        restTemplate.postForEntity("http://localhost:" + port + "/signin/", firstUser, Game.class);

        //when
        ResponseEntity<Game> response = restTemplate.postForEntity("http://localhost:" + port + "/signin/id", secondUser, Game.class);

        //then
        assertThat(response.getStatusCode(),is(HttpStatus.OK));
        assertThat(response.getBody(),is(new Game("id",List.of(new Player("Jörg"), new Player("Hans")))));
    }





}