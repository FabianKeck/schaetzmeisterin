package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.bet.BetSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class FoldHandlerTest {
    
    private final FoldHandler foldHandler = new FoldHandler();

    @Test
    @DisplayName("Fold should set the players fold status to true and advance activePlayerIndex, if player is active")
    public void FoldTest(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();

        //when
        foldHandler.handle(betSession,betSession.getPlayers().get(0).getId(), null);

        //then
        assertThat(betSession.getActivePlayerIndex(),is(1));
        assertThat(betSession.getPlayers().get(0).isFolded(),is(true));
    }

    @Test
    @DisplayName("Fold should not change bat session and throw, when player is not active")
    public void FoldUnactivePlayerTest(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();

        //when
        assertThrows(ResponseStatusException.class,()->foldHandler.handle(betSession,betSession.getPlayers().get(1).getId(), null));

        //then
        assertThat(betSession.getActivePlayerIndex(),is(0));
        assertThat(betSession.getPlayers().get(0).isFolded(),is(false));
    }



}