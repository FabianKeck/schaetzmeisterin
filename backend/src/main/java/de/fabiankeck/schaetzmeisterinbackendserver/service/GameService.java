package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.Handler.AskHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.Handler.GuessHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.Handler.PlaceBetHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.GameDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.model.*;
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

    private final AskHandler askHandler;
    private final GuessHandler guessHandler;
    private final PlaceBetHandler placeBetHandler;


    @Autowired
    public GameService(GameDao gameDao, IdUtils idUtils, SmUserDao userDao, BetSessionService betSessionService, AskHandler askHandler, GuessHandler guessHandler, PlaceBetHandler placeBetHandler) {
        this.gameDao = gameDao;
        this.idUtils = idUtils;
        this.userDao = userDao;
        this.betSessionService = betSessionService;
        this.askHandler = askHandler;
        this.guessHandler = guessHandler;
        this.placeBetHandler = placeBetHandler;
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

    public Game getGame(String gameId, String userId) {
        return getGameWithValidUser(gameId,userId);
    }

    public Game ask(String gameId, String userId, Question question) {
        Game game = getGameWithValidUser(gameId,userId);
        askHandler.handle(game.getBetSession(),userId,question);
        gameDao.save(game);
        return game;
    }

    public Game guess(String gameId, String userId, double guess) {
        Game game = getGameWithValidUser(gameId,userId);
        guessHandler.handle(game.getBetSession(),userId,guess);
        gameDao.save(game);
        return game;
    }

    public Game bet(String gameId, String userId, int betValue) {
        Game game = getGameWithValidUser(gameId,userId);
        placeBetHandler.handle(game.getBetSession(), userId, betValue);
        gameDao.save(game);
        return game;
    }

    public Game fold(String gameId, String playerId) {
        Game game = getGameWithValidUser(gameId,playerId);
        betSessionService.fold(game.getBetSession(),playerId);
        gameDao.save(game);
        return game;
    }


    private void addPlayer(Game game, String userId){

        String username = userDao.findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND)).getUsername();
        game.getPlayers().add(Player.builder().id(userId).name(username).build());
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
        betSessionPlayers.get(0).setDealing(true);
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
