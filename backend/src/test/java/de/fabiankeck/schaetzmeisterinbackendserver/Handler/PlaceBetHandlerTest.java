package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class PlaceBetHandlerTest {

    private final PlaceBetHandler placeBetHandler = new PlaceBetHandler();

    @Test
    @DisplayName("Bet with valid parameters should update Players bet and move ActivePlayer Index by 1")
    public void betTestValid(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        int betValue =3;

        //when
        placeBetHandler.handle(betSession,betSession.getPlayers().get(0).getId(),betValue);

        //then
        assertThat(betSession.getPlayers().get(0).getCurrentBet(),is(betValue));
        assertThat(betSession.getPlayers().get(0).getCash(),is(100-betValue));
        assertThat(betSession.getActivePlayerIndex(),is(1));


    }

    @Test
    @DisplayName(" Bet with invalid user should throw and return unchanged Game")
    public void BetWithInvalidUserTest(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        int betValue =3;

        //when
        assertThrows(ResponseStatusException.class,()->placeBetHandler.handle(betSession,betSession.getPlayers().get(1).getId(), betValue));
        //then
        assertThat(betSession,is(BetSessionCreationHelper.getBetSessionWithThreePlayers()));
    }

    @Test
    @DisplayName(" Bet with too large betValue user should throw and return unchanged Game")
    public void BetWithTooLargeBetValue(){
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        int betValue =101;

        assertThrows(ResponseStatusException.class,()->placeBetHandler.handle(betSession,betSession.getPlayers().get(0).getId(), betValue));
        assertThat(betSession,is(BetSessionCreationHelper.getBetSessionWithThreePlayers()));
    }

    @Test
    @DisplayName(" Bet with too small betValue user should throw and return unchanged Game")
    public void BetWithTooSmallBetValue(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setCurrentBet(6);
        betSession.setActivePlayerIndex(1);
        int betValue =5;

        //when
        assertThrows(ResponseStatusException.class,()->placeBetHandler.handle(betSession,betSession.getPlayers().get(1).getId(), betValue));

        //then
        assertThat(betSession.getActivePlayerIndex(),is(1));
        assertThat(betSession.getPlayers().get(1).getCash(),is(100));
    }


}