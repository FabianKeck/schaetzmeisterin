package de.fabiankeck.schaetzmeisterinbackendserver.game;

import de.fabiankeck.schaetzmeisterinbackendserver.game.Game;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GameDao  extends PagingAndSortingRepository<Game,String> {
}
