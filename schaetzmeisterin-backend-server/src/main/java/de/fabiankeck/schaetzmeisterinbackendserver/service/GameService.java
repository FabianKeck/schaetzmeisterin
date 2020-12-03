package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.dao.GameDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSessionPlayer;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class GameService {
    private final GameDao gameDao;
    private final IdUtils idUtils;
    private final SmUserDao userDao;
    private final BetSessionService betSessionService;


    @Autowired
    public GameService(GameDao gameDao, IdUtils idUtils, SmUserDao userDao, BetSessionService betSessionService) {
        this.gameDao = gameDao;
        this.idUtils = idUtils;
        this.userDao = userDao;
        this.betSessionService = betSessionService;
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
        Game game = getGameWithValidUser(gameId,userId);
        game.setStarted(true);
        game.setBetSession(initBetSession(game));
        gameDao.save(game);
        return  game;
    }

    public Game bet(String gameId, String userId, int betValue) {
        Game game = getGameWithValidUser(gameId,userId);
        betSessionService.bet(game.getBetSession(), userId, betValue);
        gameDao.save(game);
        return game;
    }

    public Game getGame(String gameId, String userId) {
        return getGameWithValidUser(gameId,userId);
    }

    public Game fold(String gameId, String playerId) {
        Game game = getGameWithValidUser(gameId,playerId);
        betSessionService.fold(game.getBetSession(),playerId);
        return game;
    }


    private void addPlayer(Game game, String userId){

        String username = userDao.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND)).getUsername();
        game.getPlayers().add(Player.builder().id(userId).name(username).cash(100).build());
        gameDao.save(game);
    }

    private Game initNewGame() {
        return Game.builder()
                .id(idUtils.createId())
                .players(new ArrayList<>())
                .build();
    }

    private BetSession initBetSession(Game game) {
        List<BetSessionPlayer> betSessionPlayers = game.getPlayers().stream()
                .map(player -> BetSessionPlayer.builder()
                        .id(player.getId())
                        .name(player.getName())
                        .cash(100)
                        .build())
                .collect(Collectors.toList());
        return BetSession.builder().players(betSessionPlayers).build();
    }


    private Game getGameByIdOrInit(Optional<String> gameId){
        if(gameId.isEmpty()){
            return initNewGame();
        }
        return gameDao.findById(gameId.get()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private Game getGameWithValidUser(String gameId, String userId){
        Game game = gameDao.findById(gameId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
        game.getPlayers().stream().filter((player -> player.getId().equals(userId))).findAny().orElseThrow(()->new ResponseStatusException(HttpStatus.FORBIDDEN));
        return game;
    }

}
