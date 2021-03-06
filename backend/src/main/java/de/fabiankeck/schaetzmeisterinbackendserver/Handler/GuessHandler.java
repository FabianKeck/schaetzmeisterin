package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import org.springframework.stereotype.Component;

@Component
public class GuessHandler extends BetActionHandler<Double>{

    @Override
    protected boolean isActionAllowed() {
        return !(player.isDealing() || betSession.getQuestion()==null || player.isGuessed());
    }

    @Override
    protected void handleAction() {
        player.setGuessed(true);
        player.setGuess(actionParameter);
    }

    @Override
    protected void setNextPlayerActive() {
        //don't set next Player active
    }

    @Override
    protected void evaluateBetSessionIfNecessary() {
        // no Evaluation necessary
    }
}
