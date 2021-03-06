package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSessionPlayer;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.BetSessionEvaluationHelper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BetActionHandler<T> {
    BetSession betSession;
    protected BetSessionPlayer player;
    protected T actionParameter;



    public void handle(BetSession betSession, String id, T actionParameter ){
        this.betSession = betSession;
        this.player = getPlayerIfPresentOrThrow(id);
        this.actionParameter = actionParameter;
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

    protected void setNextPlayerActive(){
        if(betSession.getPlayers().stream().filter(player->!player.isFolded()).count()<=1){ //this can be removed, when the BetSessionEvaluation is implemented
            throw new IllegalArgumentException("There are no players, that have not folded.");
        }
        int nextPlayerIndex = (betSession.getActivePlayerIndex()+1) % betSession.getPlayers().size();
        while (betSession.getPlayers().get(nextPlayerIndex).isFolded()|| betSession.getPlayers().get(nextPlayerIndex).isDealing()){
            nextPlayerIndex = (nextPlayerIndex+1)% betSession.getPlayers().size();
        }
        betSession.setActivePlayerIndex(nextPlayerIndex);

    }
    protected void evaluateBetSessionIfNecessary(){
        BetSessionEvaluationHelper.evaluateBetSessionIfNecessary(betSession);
    }

}
