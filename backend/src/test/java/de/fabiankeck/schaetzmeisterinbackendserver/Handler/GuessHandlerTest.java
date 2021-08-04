package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.bet.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.question.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class GuessHandlerTest {
    
    private final GuessHandler guessHandler = new GuessHandler();
    @Test
    @DisplayName("guess with user who is not dealing should set playersGuess")
    public void guessSetGuessTest(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question question = Question.builder().question("'Question").answer(2.5).build();
        int guessingPlayerIndex=1;
        betSession.setQuestion(question);
        double guess = 2.5;

        //when
        guessHandler.handle(betSession,betSession.getPlayers().get(guessingPlayerIndex).getId(),guess);
        //then
        assertThat(betSession.getPlayers().get(guessingPlayerIndex).getGuess(), is(guess));
        assertThat(betSession.getPlayers().get(guessingPlayerIndex).isGuessed(), is(true));
    }

    @Test
    @DisplayName("guess with dealing User should throw")
    public void guessWithDealingUserTest(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question question = Question.builder().question("'Question").answer(2.5).build();
        int guessingPlayerIndex=0;
        betSession.setQuestion(question);
        double guess = 2.5;

        //when
        assertThrows(ResponseStatusException.class,()->guessHandler.handle(betSession,betSession.getPlayers().get(guessingPlayerIndex).getId(),guess));

        //then
        assertThat(betSession.getPlayers().get(guessingPlayerIndex).isGuessed(), is(false));
    }
    @Test
    @DisplayName("guess with guessed User should throw")
    public void guessWithGuessedUserTest(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question question = Question.builder().question("'Question").answer(2.5).build();
        int guessingPlayerIndex=1;
        betSession.setQuestion(question);
        betSession.getPlayers().get(guessingPlayerIndex).setGuessed(true);
        double initialGuess = 2.5;
        betSession.getPlayers().get(guessingPlayerIndex).setGuess(initialGuess);

        //when
        assertThrows(ResponseStatusException.class,()->guessHandler.handle(betSession,betSession.getPlayers().get(guessingPlayerIndex).getId(),3.));

        //then
        assertThat(betSession.getPlayers().get(guessingPlayerIndex).getGuess(), is(initialGuess));
    }

    @Test
    @DisplayName("Guess with nonparticipating userId should throw")
    public void guessWithNonparticipatingUserTest(){
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question question = Question.builder().question("'Question").answer(2.5).build();
        betSession.setQuestion(question);

        assertThrows(ResponseStatusException.class,()->guessHandler.handle(betSession,"wrongId",3.));

    }

}