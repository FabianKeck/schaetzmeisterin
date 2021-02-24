package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSessionPlayer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PlaceBetHandler extends BetActionHandler{
    private int betValue;
    public void handle(BetSession betSession, String playerId, int betValue) {
        this.betValue = betValue;
        this.handle(betSession,playerId);
    }

    @Override
    protected boolean isActionAllowed() {
        return  !player.isDealing() &&
                betValueIsInAcceptableRange() &&
                betSession.getPlayers().get(betSession.getActivePlayerIndex()) == player;

    }

    @Override
    protected void handleAction() {
        player.setCurrentBet(player.getCurrentBet()+betValue);
        player.setCash(player.getCash()-betValue);
        player.setBetted(true);
    }

    @Override
    protected void evaluateBetSessionIfNecessary() {

    }

    private boolean betValueIsInAcceptableRange(){
        boolean betValueIsSmallerThanOrEqualsPlayerCash = this.betValue <= this.player.getCash();
        boolean betValueIsLargerThanOrEqualsMinimumBet = this.betValue >= this.betSession.getPlayers().
                stream().
                map((BetSessionPlayer::getCurrentBet))
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND))
                - this.player.getCurrentBet();
        return betValueIsSmallerThanOrEqualsPlayerCash && betValueIsLargerThanOrEqualsMinimumBet;
    }

}
