package de.fabiankeck.schaetzmeisterinbackendserver.Service;

import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GameService {
    private Game game;


    public Game initNewGame(SignInUserDto signInUserDto) {
        Player player = new Player(signInUserDto.getName());
        game= new Game(List.of(player));
        return game;
    }

}
