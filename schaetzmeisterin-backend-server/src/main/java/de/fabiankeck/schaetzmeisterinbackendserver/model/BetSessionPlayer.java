package de.fabiankeck.schaetzmeisterinbackendserver.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BetSessionPlayer {
    private String id;
    private String name;
    private int cash;
    private int currentBet;
    private boolean folded;
}
