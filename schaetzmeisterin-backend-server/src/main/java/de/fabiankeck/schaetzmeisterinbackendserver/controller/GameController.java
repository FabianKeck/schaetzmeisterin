package de.fabiankeck.schaetzmeisterinbackendserver.controller;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
    public Game bet(@PathVariable String gameId, @RequestBody int betValue, Principal principal){
        return gameService.bet(gameId,principal.getName(),betValue);
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
