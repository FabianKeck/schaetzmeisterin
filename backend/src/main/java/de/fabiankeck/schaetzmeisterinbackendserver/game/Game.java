package de.fabiankeck.schaetzmeisterinbackendserver.game;

import de.fabiankeck.schaetzmeisterinbackendserver.bet.BetSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "game")
public class Game {
    private String id;
    private boolean started =false;
    private List<Player> players;
    private BetSession betSession;

}
