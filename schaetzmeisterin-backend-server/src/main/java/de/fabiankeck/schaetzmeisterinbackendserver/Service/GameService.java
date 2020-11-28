package de.fabiankeck.schaetzmeisterinbackendserver.Service;

import de.fabiankeck.schaetzmeisterinbackendserver.dao.GameDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.GameAction;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@Service
public class GameService {
    private final GameDao gameDao;
    private final IdUtils idUtils;
    private final SmUserDao userDao;


    @Autowired
    public GameService(GameDao gameDao, IdUtils idUtils, SmUserDao userDao) {
        this.gameDao = gameDao;
        this.idUtils = idUtils;
        this.userDao = userDao;
    }


    public Game userSignIn(String userId, Optional<String> gameId) {
        Game game =  getGameByIdOrInit(gameId);

        if(game.isStarted()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        addPlayer(game, userId);
        return game;
    }

    public Game startGame(String gameId, String userId) {
        Game game = getGameWithVaildUser(gameId,userId);
        game.setStarted(true);
        gameDao.save(game);
        return  game;
    }



    private void addPlayer(Game game, String userId){

        String username = userDao.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND)).getUsername();
        game.getPlayers().add(Player.builder().id(userId).name(username).gameAction(GameAction.WAIT).build());
        gameDao.save(game);
    }

    private Game initNewGame() {
        return Game.builder()
                .id(idUtils.createId())
                .players(new ArrayList<>())
                .build();
    }


    private Game getGameByIdOrInit(Optional<String> gameId){
        if(gameId.isEmpty()){
            return initNewGame();
        }
        return gameDao.findById(gameId.get()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    private Game getGameWithVaildUser(String gameId, String userId){
        Game game = gameDao.findById(gameId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        game.getPlayers().stream().filter((player -> player.getId().equals(userId))).findAny().orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN));
        return game;
    }

}
