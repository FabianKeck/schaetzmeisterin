package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;

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
    protected void evaluateBetSessionIfNecessary() {
        // no Evaluation necessary
    }
}
