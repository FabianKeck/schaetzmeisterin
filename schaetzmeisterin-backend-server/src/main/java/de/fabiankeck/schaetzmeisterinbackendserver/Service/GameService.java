package de.fabiankeck.schaetzmeisterinbackendserver.Service;

import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.GameAction;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Question;
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


    public Game userSignIn(String userId, Optional<String> gameId) {
        //ToDo remove unused Variable username
        Game game =  getGameById(gameId);

        if(game.isStarted()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        addPlayer(game, userId);
        return game;
    }

    public Game startGame(String gameId, String userId) {
        Game game= getGameById(gameId);
        game.getPlayerActions().keySet().stream().filter(id-> Objects.equals(id,userId)).findAny()
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.FORBIDDEN));
        game.setStarted(true);
        return  game;
    }
    public Game ask(String gameId, String userId, Question question) {
        Game game = getGameById(gameId);
        game.getPlayerActions().keySet().stream().filter(id-> Objects.equals(id,userId)).findAny().orElseThrow( ()-> new ResponseStatusException(HttpStatus.FORBIDDEN));
        if(game.getPlayerActions().get(userId).equals(GameAction.ASK)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        game.getCurrentQuestionRound().setQuestion(question);
        return game;
    }


    private void addPlayer(Game game, String userId){
        HashMap<String,GameAction> newPlayerActions = new HashMap<>(game.getPlayerActions());
        newPlayerActions.put(userId, GameAction.WAIT);
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
