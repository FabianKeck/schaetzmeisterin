package de.fabiankeck.schaetzmeisterinbackendserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Game {
    private String id;
    private Map<Player,GameAction> playerActions;
    private boolean started =false;
}
