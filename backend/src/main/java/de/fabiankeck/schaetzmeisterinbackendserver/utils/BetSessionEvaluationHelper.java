package de.fabiankeck.schaetzmeisterinbackendserver.utils;

import de.fabiankeck.schaetzmeisterinbackendserver.bet.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.bet.BetSessionPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BetSessionEvaluationHelper {

    public static void evaluateBetSessionIfNecessary(BetSession betSession) {
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
    private static void finishBetSessionandDeclareWinner(BetSession betSession, BetSessionPlayer winner){
        winner.setWinner(true);
        winner.setCash(winner.getCash()+getPot(betSession));
        betSession.setFinished(true);
    }

    private static int getPot(BetSession betSession){
        return betSession.getPlayers().stream().mapToInt(BetSessionPlayer::getCurrentBet).sum();
    }

}
