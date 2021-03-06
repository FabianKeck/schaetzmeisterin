package de.fabiankeck.schaetzmeisterinbackendserver.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BetSession {
    private int activePlayerIndex;
    private List<BetSessionPlayer> players;
    private Question question;
    private boolean finished;
}
