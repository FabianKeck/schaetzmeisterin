package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

public class FoldHandler extends BetActionHandler<Void>{
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
