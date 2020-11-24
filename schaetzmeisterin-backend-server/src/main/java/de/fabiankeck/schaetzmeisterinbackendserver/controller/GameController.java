package de.fabiankeck.schaetzmeisterinbackendserver.controller;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import org.springframework.security.core.parameters.P;
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
    public Game signIn(@PathVariable Optional<String> gameId, @RequestBody SignInUserDto username, Principal principal){

        return gameService.userSignIn(principal.getName(), username.getName(), gameId );
    }

    @PostMapping("startgame/{gameId}")
    public Game startGame(@PathVariable String gameId, Principal principal){
        return gameService.startGame(gameId,principal.getName());
    }

    /*
    post quesiton
    answer
    bid /fold raise check //enum
     */
}
