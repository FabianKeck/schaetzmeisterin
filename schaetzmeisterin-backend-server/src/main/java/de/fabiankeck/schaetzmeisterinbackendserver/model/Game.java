package de.fabiankeck.schaetzmeisterinbackendserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    String id;
    List<Player> players;
}
