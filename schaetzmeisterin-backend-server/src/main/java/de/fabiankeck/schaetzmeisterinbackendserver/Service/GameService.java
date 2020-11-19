package de.fabiankeck.schaetzmeisterinbackendserver.Service;

import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class GameService {
    private final List<Game> games = new ArrayList<>();
    private final IdUtils idUtils;

    public GameService(IdUtils idUtils) {
        this.idUtils = idUtils;
    }


    public Game userSignIn(Optional<String> gameId, SignInUserDto signInUserDto) {
        Game game =  getGameById(gameId);

        addPlayer(game, new Player(signInUserDto.getName()));
        return game;
    }

    private Game getGameById(Optional<String> gameId){
        if(gameId.isEmpty()){
            return initNewGame();
        }
        return games.stream()
                .filter(game -> Objects.equals(gameId.get(),game.getId()))
                .findAny()
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private void addPlayer(Game game, Player player){
        List<Player> newPlayers = new ArrayList<>(List.copyOf(game.getPlayers()));
        newPlayers.add(player);
        game.setPlayers(newPlayers);
    }

    private Game initNewGame() {
        Game newGame = new Game(idUtils.createId(), List.of());
        games.add(newGame);
        return newGame;
    }
}
