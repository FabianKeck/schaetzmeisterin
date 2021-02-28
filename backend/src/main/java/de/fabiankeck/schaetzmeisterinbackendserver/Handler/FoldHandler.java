package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import org.springframework.stereotype.Component;

@Component
public class FoldHandler extends BetActionHandler<Void>{

    public void handle(BetSession betSession, String id) {
        super.handle(betSession, id, actionParameter);
    }

    @Override
    protected boolean isActionAllowed() {
        return !player.isDealing() && betSession.getPlayers().get(betSession.getActivePlayerIndex()) == player;
    }

    @Override
    protected void handleAction() {
        player.setBetted(true);
        player.setFolded(true);
    }
}
