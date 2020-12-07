package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BetSessionServiceTest {
    private final BetSessionService betSessionService = new BetSessionService();

    @Test
    @DisplayName("Bet with valid parameters should update Players bet and move ActivePlayer Index by 1")
    public void betTestValid(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        int betValue =3;

        //when
        betSessionService.bet(betSession,betSession.getPlayers().get(0).getId(),betValue);

        //then
        assertThat(betSession.getPlayers().get(0).getCurrentBet(),is(betValue));
        assertThat(betSession.getPlayers().get(0).getCash(),is(100-betValue));
        assertThat(betSession.getActivePlayerIndex(),is(1));


    }

    @Test
    @DisplayName(" Bet with invalid user should throw and return unchanged Game")
    public void BetWithInvalidUserTest(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        int betValue =3;

        //when
        assertThrows(ResponseStatusException.class,()->betSessionService.bet(betSession,betSession.getPlayers().get(1).getId(), betValue));
        //then
        assertThat(betSession,is(getBetSessionWithThreePlayers()));
    }

    @Test
    @DisplayName(" Bet with too large betValue user should throw and return unchanged Game")
    public void BetWithTooLargeBetValue(){
        BetSession betSession = getBetSessionWithThreePlayers();
        int betValue =101;

        assertThrows(ResponseStatusException.class,()->betSessionService.bet(betSession,betSession.getPlayers().get(0).getId(), betValue));
        assertThat(betSession,is(getBetSessionWithThreePlayers()));
    }

    @Test
    @DisplayName(" Bet with too small betValue user should throw and return unchanged Game")
    public void BetWithTooSmallBetValue(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setCurrentBet(6);
        betSession.setActivePlayerIndex(1);
        int betValue =5;

        //when
        assertThrows(ResponseStatusException.class,()->betSessionService.bet(betSession,betSession.getPlayers().get(1).getId(), betValue));

        //then
        assertThat(betSession.getActivePlayerIndex(),is(1));
        assertThat(betSession.getPlayers().get(1).getCash(),is(100));
    }

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


    private BetSession getBetSessionWithThreePlayers(){
        BetSessionPlayer player1 = BetSessionPlayer.builder().id("1").name("Jane").cash(100).build();
        BetSessionPlayer player2 = BetSessionPlayer.builder().id("2").name("John").cash(100).build();
        BetSessionPlayer player3 = BetSessionPlayer.builder().id("3").name("Doe").cash(100).build();
        return BetSession.builder()
                .players(List.of(player1,player2,player3))
                .build();
    }
}