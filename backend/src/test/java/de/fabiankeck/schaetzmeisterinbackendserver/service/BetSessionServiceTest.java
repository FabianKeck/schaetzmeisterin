package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BetSessionServiceTest {
    private final BetSessionService betSessionService = new BetSessionService();


   @Test
   @DisplayName("Fold should set the players fold status to true and advance activePlayerIndex, if player is active")
   public void FoldTest(){
       //given
       BetSession betSession = getBetSessionWithThreePlayers();

       //when
       betSessionService.fold(betSession,betSession.getPlayers().get(0).getId());

       //then
       assertThat(betSession.getActivePlayerIndex(),is(1));
       assertThat(betSession.getPlayers().get(0).isFolded(),is(true));
   }

   @Test
   @DisplayName("Fold should not change bat session and throw, when player is not active")
   public void FoldUnactivePlayerTest(){
       //given
       BetSession betSession = getBetSessionWithThreePlayers();

       //when
       assertThrows(ResponseStatusException.class,()->betSessionService.fold(betSession,betSession.getPlayers().get(1).getId()));

       //then
       assertThat(betSession.getActivePlayerIndex(),is(0));
       assertThat(betSession.getPlayers().get(0).isFolded(),is(false));
   }

   @Test
   @DisplayName("folded player should be skipped, when updating active player Index")
   public void skipsFoldedUserTest(){
      //given
      BetSession betSession = getBetSessionWithThreePlayers();
      betSession.getPlayers().get(1).setFolded(true);

      //when
       betSessionService.bet(betSession,betSession.getPlayers().get(0).getId(),1);

       //then
       assertThat(betSession.getActivePlayerIndex(),is(2));

  }
    @Test
    @DisplayName("Dealing player should be skipped, when updating active player Index")
    public void skipsDealingUserTest(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(1).setDealing(true);

        //when
        betSessionService.bet(betSession,betSession.getPlayers().get(0).getId(),1);

        //then
        assertThat(betSession.getActivePlayerIndex(),is(2));
    }



    private BetSession getBetSessionWithThreePlayers(){
        BetSessionPlayer player1 = BetSessionPlayer.builder().id("1").name("Jane").cash(100).build();
        BetSessionPlayer player2 = BetSessionPlayer.builder().id("2").name("John").cash(100).build();
        BetSessionPlayer player3 = BetSessionPlayer.builder().id("3").name("Doe").cash(100).build();
        return BetSession.builder()
                .players(List.of(player1,player2,player3))
                .build();
    }
}