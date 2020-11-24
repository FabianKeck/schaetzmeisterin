package de.fabiankeck.schaetzmeisterinbackendserver.Service;

import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.GameAction;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.sound.midi.Patch;
import java.util.*;


@Service
public class GameService {
    private final List<Game> games = new ArrayList<>();
    private final IdUtils idUtils;

    public GameService(IdUtils idUtils) {
        this.idUtils = idUtils;
    }


    public Game userSignIn(String id, String userName, Optional<String> gameId) {
        Game game =  getGameById(gameId);

        //todo check whether Game is started
        addPlayer(game, new Player(id,userName));
        return game;
    }

    public Game startGame(String gameId, String id) {
        Game game= getGameById(gameId);
        game.getPlayerActions().keySet()
                .stream().
                filter((player -> Objects.equals(player.getId(),id)))
                .findAny()
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.FORBIDDEN));
        game.setStarted(true);
        return  game;
    }


    private void addPlayer(Game game, Player player){
        HashMap<Player,GameAction> newPlayerActions = new HashMap<>(game.getPlayerActions());
        newPlayerActions.put(player, GameAction.WAIT);
        game.setPlayerActions(newPlayerActions);
    }

    private Game initNewGame() {
        Game newGame =  Game.builder().id(idUtils.createId()).playerActions(new HashMap<>()).build();
        games.add(newGame);
        return newGame;
    }

    public void clearGames(){
        games.clear();
    }


    private Game getGameById(String gameId) {
        return games.stream()
                .filter(game -> Objects.equals(gameId, game.getId()))
                .findAny()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    private Game getGameById(Optional<String> gameId){
        if(gameId.isEmpty()){
            return initNewGame();
        }
        return getGameById(gameId.get());
    }
}
