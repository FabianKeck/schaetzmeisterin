package de.fabiankeck.schaetzmeisterinbackendserver.game;

import de.fabiankeck.schaetzmeisterinbackendserver.question.GuessDto;
import de.fabiankeck.schaetzmeisterinbackendserver.question.Question;
import de.fabiankeck.schaetzmeisterinbackendserver.bet.BetDto;
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
    public Game signIn(@PathVariable(required = false) String gameId, Principal principal){
        Optional<String> gameIdOptional = Optional.ofNullable(gameId);
        return gameIdOptional
                .map(id -> gameService.userSignIn(principal.getName(), id ))
                .orElse(gameService.userSignIn(principal.getName()));
    }

    @PostMapping("startgame/{gameId}")
    public Game startGame(@PathVariable String gameId, Principal principal){
        return gameService.startGame(gameId,principal.getName());
    }

    @PostMapping("ask/{gameId}")
    public Game ask(@PathVariable String gameId, Principal principal, @RequestBody Question question){
        return gameService.ask(gameId, principal.getName(),question);
    }

    @PostMapping("/guess/{gameId}")
    public Game guess(@PathVariable String gameId,  Principal principal, @RequestBody GuessDto guessDto){
        return gameService.guess(gameId, principal.getName(),guessDto.getGuess());
    }

    @PostMapping("/bet/{gameId}")
    public Game bet(@PathVariable String gameId,  Principal principal ,@RequestBody BetDto bet){
        return gameService.bet(gameId, principal.getName(), bet.getBetValue());
    }

    @PostMapping("/fold/{gameId}")
    public Game fold(@PathVariable String gameId, Principal principal){
        return gameService.fold( gameId, principal.getName());
    }

    @GetMapping("{gameId}")
    public Game getGame(@PathVariable String gameId, Principal principal){
        return gameService.getGame(gameId, principal.getName());
    }
}
