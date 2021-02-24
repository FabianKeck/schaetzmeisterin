package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSessionPlayer;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class BetActionHandler {
    BetSession betSession;
    protected BetSessionPlayer player;



    public void handle(BetSession betSession, String id){
        this.betSession = betSession;
        this.player = getPlayerIfPresentOrThrow(id);
        if (!isActionAllowed()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        handleAction();
        setNextPlayerActive();
        evaluateBetSessionIfNecessary();

    }

    private BetSessionPlayer getPlayerIfPresentOrThrow(String id) {
        return this.betSession
                .getPlayers()
                .stream()
                .filter(player -> player.getId().equals(id))
                .findAny()
                .orElseThrow( () ->  new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    protected abstract boolean isActionAllowed();

    protected abstract void handleAction();
    private void setNextPlayerActive(){

    }
    protected abstract void evaluateBetSessionIfNecessary();

}
