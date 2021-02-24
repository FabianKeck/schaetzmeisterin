package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSessionPlayer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PlaceBetHandler extends BetActionHandler<Integer>{

    @Override
    protected boolean isActionAllowed() {
        return  !player.isDealing() &&
                betValueIsInAcceptableRange() &&
                betSession.getPlayers().get(betSession.getActivePlayerIndex()) == player;

    }

    @Override
    protected void handleAction() {
        player.setCurrentBet(player.getCurrentBet()+ actionParameter);
        player.setCash(player.getCash()- actionParameter);
        player.setBetted(true);
    }

    private boolean betValueIsInAcceptableRange(){
        boolean betValueIsSmallerThanOrEqualsPlayerCash = this.actionParameter <= this.player.getCash();
        boolean betValueIsLargerThanOrEqualsMinimumBet = this.actionParameter >= this.betSession.getPlayers().
                stream().
                map((BetSessionPlayer::getCurrentBet))
                .mapToInt(Integer::intValue)
                .max()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND))
                - this.player.getCurrentBet();
        return betValueIsSmallerThanOrEqualsPlayerCash && betValueIsLargerThanOrEqualsMinimumBet;
    }

}
