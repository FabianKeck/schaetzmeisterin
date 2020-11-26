package de.fabiankeck.schaetzmeisterinbackendserver.Service;

import de.fabiankeck.schaetzmeisterinbackendserver.dao.GameDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.GameAction;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Question;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
public class GameService {
    private final GameDao gameDao;
    private final IdUtils idUtils;
    private final SmUserDao userDao;

    public GameService(GameDao gameDao, IdUtils idUtils, SmUserDao userDao) {
        this.gameDao = gameDao;
        this.idUtils = idUtils;
        this.userDao = userDao;
    }


    public Game userSignIn(String userId, Optional<String> gameId) {
        Game game =  getGameById(gameId);

        if(game.isStarted()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        addPlayer(game, userId);
        return game;
    }

    public Game startGame(String gameId, String userId) {
        Game game = gameDao.findById(gameId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        game.getPlayerActions().keySet().stream().filter(id-> Objects.equals(id,userId)).findAny()
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.FORBIDDEN));
        game.setStarted(true);
        gameDao.save(game);
        return  game;
    }
    public Game ask(String gameId, String userId, Question question) {
        Game game = gameDao.findById(gameId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
        game.getPlayerActions().keySet().stream().filter(id-> Objects.equals(id,userId)).findAny().orElseThrow( ()-> new ResponseStatusException(HttpStatus.FORBIDDEN));
        if(game.getPlayerActions().get(userId).equals(GameAction.ASK)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        game.getCurrentQuestionRound().setQuestion(question);
        return game;
    }


    private void addPlayer(Game game, String userId){
        String username = userDao.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND)).getUsername();
        game.getPlayerNames().put(userId,username);
        game.getPlayerActions().put(userId, GameAction.WAIT);
        gameDao.save(game);
    }

    private Game initNewGame() {
        return Game.builder()
                .id(idUtils.createId())
                .playerActions(new HashMap<>())
                .playerNames(new HashMap<>())
                .build();
    }


    private Game getGameById(Optional<String> gameId){
        if(gameId.isEmpty()){
            return initNewGame();
        }
        return gameDao.findById(gameId.get()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

}
