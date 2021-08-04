package de.fabiankeck.schaetzmeisterinbackendserver.bet;


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
    private boolean dealing;
    private double guess;
    private boolean guessed;
    private boolean betted;
    private boolean winner;
}
