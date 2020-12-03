package de.fabiankeck.schaetzmeisterinbackendserver.controller;

import de.fabiankeck.schaetzmeisterinbackendserver.service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.BetDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping({"/signin/{gameId}","/signin"})
    public Game signIn(@PathVariable Optional<String> gameId, Principal principal){
        return gameService.userSignIn(principal.getName(), gameId );
    }

    @PostMapping("startgame/{gameId}")
    public Game startGame(@PathVariable String gameId, Principal principal){
        return gameService.startGame(gameId,principal.getName());
    }

    @PostMapping("/bet/{gameId}")
    public Game bet(@PathVariable String gameId, @RequestBody BetDto bet, Principal principal){
        return gameService.bet(gameId,principal.getName(),bet.getBetValue());
    }

    @PostMapping("/fold/{gameId}")
    public Game fold(@PathVariable String gameId, Principal principal){
        return gameService.fold( gameId, principal.getName());
    }

    @GetMapping("{gameId}")
    public Game getGame(@PathVariable String gameId, Principal principal){
        return gameService.getGame(gameId,principal.getName());
    }




    /*
    post quesiton
    answer
    bid /fold raise check //enum
     */
}
