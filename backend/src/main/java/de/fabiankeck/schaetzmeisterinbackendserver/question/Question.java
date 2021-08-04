package de.fabiankeck.schaetzmeisterinbackendserver.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Question {
    private String question;
    private double answer;
}
