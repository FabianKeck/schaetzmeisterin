package de.fabiankeck.schaetzmeisterinbackendserver.controller;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/signin")
public class SignInController {
    private final GameService gameService;

    public SignInController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping({"/","/{gameId}"})
    public Game signIn(@PathVariable Optional<String> gameId,  @RequestBody SignInUserDto signInUserDto){
        return gameService.userSignIn(gameId, signInUserDto);
    }
}
