package de.fabiankeck.schaetzmeisterinbackendserver.controller;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signin")
public class SignInController {
    private final GameService gameService;

    public SignInController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping
    public Game signIn(@RequestBody SignInUserDto signInUserDto){

        return gameService.initNewGame(signInUserDto);
    }
}
