package de.fabiankeck.schaetzmeisterinbackendserver.Handler;

import de.fabiankeck.schaetzmeisterinbackendserver.model.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;

class AskHandlerTest {
    private AskHandler askHandler = new AskHandler();

    @Test
    @DisplayName("ask should set question")
    public void askSetQuestionTest(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question expected= Question.builder().question("'Question").answer(2.5).build();

        //when
        askHandler.handle(betSession,betSession.getPlayers().get(0).getId(),expected);
        Question actual = betSession.getQuestion();
        //then
        assertThat(actual.getAnswer(), is(expected.getAnswer()));
        assertThat(actual.getQuestion(), is(expected.getQuestion()));

    }
    @Test
    @DisplayName("Ask should throw if player is not dealing")
    public void askWithWrongPlayerTest(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question expected= Question.builder().question("'Question").answer(2.5).build();

        //when
        assertThrows(ResponseStatusException.class,()->askHandler.handle(betSession,betSession.getPlayers().get(1).getId(),expected));
        //then
        assertThat(betSession.getQuestion(), is(nullValue()));
    }
    @Test
    @DisplayName("Ask should throw if question is already defined")
    public void askWithQuestionPlayedTest(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question firstQuestion= Question.builder().question("'Question").answer(2.5).build();
        betSession.setQuestion(firstQuestion);
        Question newQuestion = Question.builder().question("another Question").answer(0).build();


        //when
        assertThrows(ResponseStatusException.class,()->askHandler.handle(betSession,betSession.getPlayers().get(0).getId(),newQuestion));
        //then
        assertThat(betSession.getQuestion(), is(firstQuestion));
    }
    @Test
    @DisplayName("handle sets activePlayerIndex to the player after the Dealer")
    public void askSetsActivePlayerIndexCorrectly(){
        //given
        BetSession betSession = BetSessionCreationHelper.getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question expected= Question.builder().question("'Question").answer(2.5).build();

        //when
        askHandler.handle(betSession,betSession.getPlayers().get(0).getId(),expected);

        assertThat(betSession.getActivePlayerIndex(), is(1));
    }
}