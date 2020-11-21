package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
        when(idUtils.createId()).thenReturn("id");
        Game actual = gameService.userSignIn("Fabian",Optional.empty());
        Game expected = new Game( "id", List.of(new Player("Fabian")));
        //then
        assertThat(actual,is(expected));
        verify(idUtils).createId();
    }
    @Test
    @DisplayName("userSignIn with GameId should return the updated game")
    public void userSignInTestWithId(){
        //given
        String oldUser = "Jan";
        when(idUtils.createId()).thenReturn("id");
        Game oldGame = gameService.userSignIn(oldUser,Optional.empty());

        String newUser = "Fabian";


        //when
        Game actual = gameService.userSignIn(newUser,Optional.of("id"));
        //then
        assertThat(actual.getId(),is("id"));
        assertThat(actual.getPlayers(),containsInAnyOrder(new Player("Jan"), new Player("Fabian") ));
    }
  @Test
    @DisplayName("userSignIn with invalid GameId should throw Httpstatus-exception")
    public void signInWithInvalidId(){
        //given
        String signInUserDto = "Jan";
        //when

      try {
          gameService.userSignIn(signInUserDto,Optional.of("id"));
          fail();
      } catch (Exception e) {
          assertThat(e.getMessage(),is(HttpStatus.NOT_FOUND.toString()));
      }
    }
}