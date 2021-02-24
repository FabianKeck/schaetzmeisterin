package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Question;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AskHandler extends BetActionHandler<Question>{


    @Override
    protected boolean isActionAllowed() {
        return player.isDealing() && betSession.getQuestion() == null;
    }

    @Override
    protected void handleAction() {
        this.betSession.setQuestion(this.actionParameter);
    }

    @Override
    protected void evaluateBetSessionIfNecessary() {

    }
}
