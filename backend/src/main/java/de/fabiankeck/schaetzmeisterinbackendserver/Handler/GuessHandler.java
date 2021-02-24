package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSessionPlayer;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GuessHandler extends BetActionHandler{
    double guess;
    public void handle(BetSession betSession, String playerId, double guess){
        this.guess= guess;
        this.handle(betSession,playerId);
    }

    @Override
    protected boolean isActionAllowed() {
        return !(player.isDealing() || betSession.getQuestion()==null || player.isGuessed());
    }

    @Override
    protected void handleAction() {
        player.setGuessed(true);
        player.setGuess(guess);
    }

    @Override
    protected void evaluateBetSessionIfNecessary() {
        // no Evaluation necessary
    }
}
