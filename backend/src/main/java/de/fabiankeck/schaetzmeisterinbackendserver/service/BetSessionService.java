package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.Handler.AskHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.Handler.GuessHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.Handler.PlaceBetHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.channels.spi.AbstractSelectionKey;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BetSessionService {
    private final GuessHandler guessHandler = new GuessHandler();
    private final AskHandler askHandler = new AskHandler();
    private final PlaceBetHandler placeBetHandler = new PlaceBetHandler();

    public void ask(BetSession betSession, String playerId, Question question) {
        askHandler.handle(betSession, playerId, question);
    }

    public void guess(BetSession betSession, String playerId, double guess) {
        guessHandler.handle(betSession, playerId, guess);
    }

    public void bet(BetSession betSession, String playerId, int betValue){
        placeBetHandler.handle(betSession, playerId, betValue);
    }
    public void fold(BetSession betSession, String playerId){
        BetSessionPlayer player = getPlayerIfActiveOrThrow(betSession,playerId);
        player.setBetted(true);
        player.setFolded(true);
        evaluateBetSessionIfNecessary(betSession);
        if(betSession.isFinished()){
            return;
        }

        markNextPlayerActive(betSession);
    }

    private void markNextPlayerActive(BetSession betSession) {
        if(betSession.getPlayers().stream().filter(player->!player.isFolded()).count()<=1){ //this can be removed, when the BetSessionEvaluation is implemented
            throw new IllegalArgumentException("There are no players, that have not folded left");
        }
        int nextPlayerIndex = (betSession.getActivePlayerIndex()+1) % betSession.getPlayers().size();
        while (betSession.getPlayers().get(nextPlayerIndex).isFolded()|| betSession.getPlayers().get(nextPlayerIndex).isDealing()){
            nextPlayerIndex = (nextPlayerIndex+1)% betSession.getPlayers().size();
        }
        betSession.setActivePlayerIndex(nextPlayerIndex);
    }


    private BetSessionPlayer getPlayerIfActiveOrThrow(BetSession betSession ,String playerId){
        BetSessionPlayer player = betSession.getPlayers().get(betSession.getActivePlayerIndex());

        if( !player.getId().equals(playerId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return player;
    }
    private BetSessionPlayer getPlayerIfPresentOrThrow(BetSession betSession, String playerID){
        return betSession.getPlayers().stream().filter(betSessionPlayer -> betSessionPlayer.getId().equals(playerID)).findAny().orElseThrow(()-> new ResponseStatusException(HttpStatus.FORBIDDEN));
    }
    public void evaluateBetSessionIfNecessary(BetSession betSession) {
        boolean allPlayersHaveBeenActive = betSession.getPlayers().stream()
                .allMatch(player -> player.isDealing() || player.isBetted());
        if (!allPlayersHaveBeenActive) return;

        List<BetSessionPlayer> playersStillBetting = betSession.getPlayers().stream().filter(player -> !player.isDealing()).filter(player->!player.isFolded()).collect(Collectors.toList());
        //all players bet is equal ?
        if(playersStillBetting.size()==1){
            finishBetSessionandDeclareWinner(betSession,playersStillBetting.get(0));
            return;
        }
        int aBet = playersStillBetting.get(0).getCurrentBet();
        if (playersStillBetting.stream().anyMatch(player -> player.getCurrentBet() != aBet)) return;


        BetSessionPlayer winner = playersStillBetting.stream().min(Comparator.comparingDouble(player -> Math.abs(player.getGuess() - betSession.getQuestion().getAnswer()))).orElseThrow(() -> new IllegalArgumentException("No Players here"));
       finishBetSessionandDeclareWinner(betSession,winner);
    }
    private void finishBetSessionandDeclareWinner(BetSession betSession, BetSessionPlayer winner){
        winner.setWinner(true);
        winner.setCash(winner.getCash()+getPot(betSession));
        betSession.setFinished(true);
    }

    private int getPot(BetSession betSession){
        return betSession.getPlayers().stream().mapToInt(BetSessionPlayer::getCurrentBet).sum();
    }

}
