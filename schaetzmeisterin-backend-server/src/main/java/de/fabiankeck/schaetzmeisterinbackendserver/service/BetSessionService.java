package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BetSessionService {

    public void ask(BetSession betSession, String playerId, Question question) {
        BetSessionPlayer player = getPlayerIfPresentOrThrow(betSession,playerId);
        if(!player.isDealing() || betSession.getQuestion()!=null){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        markNextPlayerActive(betSession);
        betSession.setQuestion(question);
    }

    public void guess(BetSession betSession, String playerId, double guess) {
        BetSessionPlayer player = getPlayerIfPresentOrThrow(betSession,playerId);
        if(player.isDealing() || betSession.getQuestion()==null || player.isGuessed()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        player.setGuessed(true);
        player.setGuess(guess);
    }

    public void bet(BetSession betSession, String playerId, int betValue){
        BetSessionPlayer player = getPlayerIfActiveOrThrow(betSession,playerId);

        if(!betValueIsInAcceptableRange(betSession,player,betValue) || player.isDealing()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        player.setCurrentBet(player.getCurrentBet()+betValue);
        player.setCash(player.getCash()-betValue);
        player.setBetted(true);
        evaluateBetSessionIfNecessary(betSession);

        markNextPlayerActive(betSession);
    }
    public void fold(BetSession betSession, String playerId){
        BetSessionPlayer player = getPlayerIfActiveOrThrow(betSession,playerId);
        markNextPlayerActive(betSession);
        player.setBetted(true);
        player.setFolded(true);
        evaluateBetSessionIfNecessary(betSession);
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
    private BetSessionPlayer getPlayerIfPresentOrThrow(BetSession betSession, String playerID){
        return betSession.getPlayers().stream().filter(betSessionPlayer -> betSessionPlayer.getId().equals(playerID)).findAny().orElseThrow(()-> new ResponseStatusException(HttpStatus.FORBIDDEN));
    }
    private void evaluateBetSessionIfNecessary(BetSession betSession) {
        boolean allPlayersHaveBeenActive = betSession.getPlayers().stream()
                .anyMatch(player -> !player.isDealing() && !player.isBetted());
        if (!allPlayersHaveBeenActive) return;

        List<BetSessionPlayer> playersStillBetting = betSession.getPlayers().stream().filter(player -> !player.isDealing() && !player.isFolded()).collect(Collectors.toList());
        //all players bet is equal ?
        int aBet = playersStillBetting.get(0).getCurrentBet();
        if (playersStillBetting.stream().anyMatch(player -> player.getCurrentBet() != aBet)) return;


        BetSessionPlayer winner = playersStillBetting.stream().min(Comparator.comparingDouble(player -> Math.abs(player.getGuess() - betSession.getQuestion().getAnswer()))).orElseThrow(() -> new IllegalArgumentException("No Players here"));
        winner.setCash(winner.getCash()+getPot(betSession));

        betSession.getPlayers().forEach(player->player.setCurrentBet(0));
    }
    private int getPot(BetSession betSession){
        return betSession.getPlayers().stream().mapToInt(BetSessionPlayer::getCurrentBet).sum();
    }


}
