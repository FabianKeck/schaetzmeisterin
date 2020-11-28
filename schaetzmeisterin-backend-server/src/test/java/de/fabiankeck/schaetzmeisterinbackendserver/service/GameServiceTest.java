package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.GameDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.GameAction;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.model.SmUser;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.method.P;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


class GameServiceTest {

    IdUtils idUtils = mock(IdUtils.class);
    SmUserDao userDao= mock(SmUserDao.class);
    GameDao gameDao = mock(GameDao.class);
    GameService gameService = new GameService(gameDao, idUtils, userDao);

    @Test
    @DisplayName("userSignIn with emptyGameID should return a new Game Object and call IdUtils.createID")
    public void userSignInTest(){
        //given

        String gameId= "gameId";
        SmUser user = SmUser.builder()
                .id("123")
                .username("John")
                .build();
        when(idUtils.createId()).thenReturn(gameId);

        Game expected = Game.builder()
                .id( gameId)
                .players(List.of(Player.builder().id(user.getId()).name(user.getUsername()).build()))
                .build();

        //when

        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        when(gameDao.save(expected)).thenReturn(expected);
        Game actual = gameService.userSignIn("123" ,Optional.empty());
        //then
        assertThat(actual,is(expected));
        verify(idUtils).createId();
        verify(gameDao).save(expected);
    }
    @Test
    @DisplayName("userSignIn with GameId should return the updated game")
    public void userSignInTestWithId(){
        //given
        String gameId ="gameId";
        SmUser initialUser = SmUser.builder().id("123").username("Jane").build();
        SmUser userToAdd = SmUser.builder().id("456").username("John").build();
        when(idUtils.createId()).thenReturn(gameId);
        Game initial= Game.builder()
                .id(gameId)
                .players(new ArrayList<>(List.of(Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build())))
                .build();
        Game updated =  Game.builder()
                .id(gameId)
                .players(List.of(
                        Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build(),
                        Player.builder().id(userToAdd.getId()).name(userToAdd.getUsername()).build()
                ))
                .build();
        //when
        when(gameDao.findById(gameId)).thenReturn(Optional.of(initial));
        when(gameDao.save(updated)).thenReturn(updated);
        when(userDao.findById(userToAdd.getId())).thenReturn(Optional.of(userToAdd));
        Game actual = gameService.userSignIn(userToAdd.getId(),Optional.of(gameId));
        //then
        assertThat(actual.getId(),is(gameId));
        assertThat(actual.getPlayers().stream().map(Player::getId).collect(Collectors.toList()), containsInAnyOrder("123","456"));
        verify(gameDao).findById(gameId);
        verify(gameDao).save(updated);
    }
  @Test
    @DisplayName("userSignIn with invalid GameId should throw Httpstatus-exception")
    public void signInWithInvalidId(){
        //when

      try {
          gameService.userSignIn("123",Optional.of("id"));
          fail();
      } catch (Exception e) {
          assertThat(e.getMessage(),is(HttpStatus.NOT_FOUND.toString()));
      }
    }

    @Test
    @DisplayName("startGame with vaild userId should return updated Game")
    public void startGameTest(){
        String gameId ="gameId";
        SmUser initialUser = SmUser.builder().id("123").username("Jane").build();
        Game initial= Game.builder()
                .id(gameId)
                .players(List.of(Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build()))
                .build();
        when(gameDao.findById(gameId)).thenReturn(Optional.of(initial));

        //when
        Game actual = gameService.startGame(gameId, initialUser.getId());

        //then
        Game expected = Game.builder()
                .id(gameId)
                .players(List.of(Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build()))
                .started(true)
                .build();
        assertThat(actual, is(expected));
        verify(gameDao).save(expected);

    }
     @Test
    @DisplayName("startGame with invalid userId should throw ")
    public void startGameInvaildUser(){
        String gameId ="gameId";
        SmUser initialUser = SmUser.builder().id("123").username("Jane").build();
        Game initial= Game.builder()
                .id(gameId)
                .players(List.of(Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build()))
                .build();
        when(gameDao.findById(gameId)).thenReturn(Optional.of(initial));

        //when
         try{
             gameService.startGame(gameId, "otherId");
             fail();
         }catch (Exception e){
             //
         }
    }

    @Test
    @DisplayName("userSignIn with started game should throw")
    void signInStartedTest(){
        String gameId ="gameId";
        SmUser initialUser = SmUser.builder().id("123").username("Jane").build();
        Game game= Game.builder()
                .id(gameId)
                .players(List.of(Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build()))
                .started(true)
                .build();
        when(gameDao.findById(gameId)).thenReturn(Optional.of(game));

        //when
        try{
            gameService.userSignIn(initialUser.getId(),Optional.of(gameId));
            fail();
        }catch (Exception e){
            //
        }

    }

}