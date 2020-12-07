package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSessionPlayer;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@Service
public class BetSessionService {

    public void bet(BetSession betSession, String playerId, int betValue){
        BetSessionPlayer player = getPlayerIfActiveOrThrow(betSession,playerId);

        if(!betValueIsInAcceptableRange(betSession,player,betValue)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        player.setCurrentBet(player.getCurrentBet()+betValue);
        player.setCash(player.getCash()-betValue);

        markNextPlayerActive(betSession);
    }
    public void fold(BetSession betSession, String playerId){
        BetSessionPlayer player = getPlayerIfActiveOrThrow(betSession,playerId);
        markNextPlayerActive(betSession);
        player.setFolded(true);
    }

    private void markNextPlayerActive(BetSession betSession) {
        if(betSession.getPlayers().stream().filter(player->!player.isFolded()).findAny().isEmpty()){ //this can be removed, when the BetSessionEvaluation is implemented
            throw new IllegalArgumentException("There are no players, that have not folded left");
        }
        int nextPlayerIndex = (betSession.getActivePlayerIndex()+1) % betSession.getPlayers().size();
        while (betSession.getPlayers().get(nextPlayerIndex).isFolded()){
            nextPlayerIndex = (nextPlayerIndex+1)% betSession.getPlayers().size();
        }
        betSession.setActivePlayerIndex(nextPlayerIndex);
    }

    private boolean betValueIsInAcceptableRange(BetSession betSession, BetSessionPlayer player, int betValue){
        boolean betValueIsSmallerThanOrEqualsPlayerCash = betValue <= player.getCash();
        boolean betValueIsLargerThanOrEqualsMinimumBet = betValue >= betSession.getPlayers().
                stream().
                map((BetSessionPlayer::getCurrentBet))
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND))
                - player.getCurrentBet();
        return betValueIsSmallerThanOrEqualsPlayerCash && betValueIsLargerThanOrEqualsMinimumBet;
    }

    private BetSessionPlayer getPlayerIfActiveOrThrow(BetSession betSession ,String playerId){
        BetSessionPlayer player = betSession.getPlayers().get(betSession.getActivePlayerIndex());

        if( !player.getId().equals(playerId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return player;
    }


}
