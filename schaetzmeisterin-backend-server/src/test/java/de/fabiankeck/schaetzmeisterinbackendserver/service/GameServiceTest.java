package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.GameAction;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


class GameServiceTest {

    IdUtils idUtils = mock(IdUtils.class);
    GameService gameService = new GameService(idUtils);

    @Test
    @DisplayName("userSignIn with emptyGameID should return a new Game Object and call IdUtils.createID")
    public void userSignInTest(){
        //given


        //when
        when(idUtils.createId()).thenReturn("GameId");
        Game actual = gameService.userSignIn("123" ,Optional.empty());
        Game expected = Game.builder().id( "GameId").playerActions(Map.of("123", GameAction.WAIT)).build();
        //then
        assertThat(actual,is(expected));
        verify(idUtils).createId();
    }
    @Test
    @DisplayName("userSignIn with GameId should return the updated game")
    public void userSignInTestWithId(){
        //given
        when(idUtils.createId()).thenReturn("GameId");
        gameService.userSignIn("123", Optional.empty());
        //when
        Game actual = gameService.userSignIn("456",Optional.of("GameId"));
        //then
        assertThat(actual.getId(),is("GameId"));
        assertThat(actual.getPlayerActions().keySet(),containsInAnyOrder("123","456"));
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
}