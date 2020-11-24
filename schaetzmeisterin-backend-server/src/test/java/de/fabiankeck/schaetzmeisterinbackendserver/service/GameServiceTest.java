package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;
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
        Game actual = gameService.userSignIn("123", "Fabian",Optional.empty());
        Game expected = new Game( "GameId", List.of(new Player("123","Fabian")));
        //then
        assertThat(actual,is(expected));
        verify(idUtils).createId();
    }
    @Test
    @DisplayName("userSignIn with GameId should return the updated game")
    public void userSignInTestWithId(){
        //given
        String oldUserName = "Jan";

        when(idUtils.createId()).thenReturn("GameId");
        gameService.userSignIn("123", oldUserName,Optional.empty());

        String newUserName = "Fabian";


        //when

        Game actual = gameService.userSignIn("456", newUserName,Optional.of("GameId"));
        //then
        assertThat(actual.getId(),is("GameId"));
        assertThat(actual.getPlayers(),containsInAnyOrder(new Player("123","Jan"), new Player("456","Fabian") ));
    }
  @Test
    @DisplayName("userSignIn with invalid GameId should throw Httpstatus-exception")
    public void signInWithInvalidId(){
        //given
        String signInUserDto = "Jan";
        //when

      try {
          gameService.userSignIn("123", signInUserDto,Optional.of("id"));
          fail();
      } catch (Exception e) {
          assertThat(e.getMessage(),is(HttpStatus.NOT_FOUND.toString()));
      }
    }
}