package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Question;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AskHandler extends BetActionHandler{
    private Question question   ;

    public void handle(BetSession betSession, String playerId, Question question) {
        this.question = question;
        this.handle(betSession, playerId);
    }

    @Override
    protected boolean isActionAllowed() {
        return player.isDealing() && betSession.getQuestion() == null;
    }

    @Override
    protected void handleAction() {
        this.betSession.setQuestion(this.question);
    }

    @Override
    protected void evaluateBetSessionIfNecessary() {

    }
}
