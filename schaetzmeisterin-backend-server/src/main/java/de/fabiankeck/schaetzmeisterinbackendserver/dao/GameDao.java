package de.fabiankeck.schaetzmeisterinbackendserver.dao;

import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GameDao  extends PagingAndSortingRepository<Game,String> {
}
