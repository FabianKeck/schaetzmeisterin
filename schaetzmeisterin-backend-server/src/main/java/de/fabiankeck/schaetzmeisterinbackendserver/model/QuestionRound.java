package de.fabiankeck.schaetzmeisterinbackendserver.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionRound {
    private Question question;
    private HashMap<Player, Double>  guesses;
}
