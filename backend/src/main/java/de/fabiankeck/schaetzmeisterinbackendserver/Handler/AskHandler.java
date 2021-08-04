package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.question.Question;
import org.springframework.stereotype.Component;

@Component
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
        // no evaluation needs to be done, after asking
    }
}
