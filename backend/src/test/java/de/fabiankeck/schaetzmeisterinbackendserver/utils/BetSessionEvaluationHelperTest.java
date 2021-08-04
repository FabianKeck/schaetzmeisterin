package de.fabiankeck.schaetzmeisterinbackendserver.utils;

import de.fabiankeck.schaetzmeisterinbackendserver.bet.BetSession;
import de.fabiankeck.schaetzmeisterinbackendserver.bet.BetSessionPlayer;
import de.fabiankeck.schaetzmeisterinbackendserver.question.Question;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BetSessionEvaluationHelperTest {
    @Test
    @DisplayName("BetSession where all Players have betted and have reached the same betValue should be evaluated to finished and the player with the best guess should be set as winner ")
    public void evaluationSameCurrentBetTest(){
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        betSession.setQuestion(Question.builder().question("?").answer(2.5).build());
        betSession.getPlayers().get(1).setGuessed(true);
        betSession.getPlayers().get(1).setGuess(1.5);
        betSession.getPlayers().get(1).setBetted(true);
        betSession.getPlayers().get(1).setCurrentBet(2);
        betSession.getPlayers().get(2).setGuessed(true);
        betSession.getPlayers().get(2).setGuess(2.5);
        betSession.getPlayers().get(2).setBetted(true);
        betSession.getPlayers().get(2).setCurrentBet(2);

        BetSessionEvaluationHelper.evaluateBetSessionIfNecessary(betSession);

        assertThat(betSession.isFinished(), is(true));
        assertThat(betSession.getPlayers().get(2).isWinner(),is(true));
        assertThat(betSession.getPlayers().get(0).isWinner(),is(false));
        assertThat(betSession.getPlayers().get(1).isWinner(),is(false));
    }

    @Test
    @DisplayName("BetSession where all Players not all Players have betted and have reached the same betValue should not declare winner and the finished status should remain false")
    public void evaluationSameCurrentBetbutNotAllPlayersHaveBettedTest(){
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        betSession.setQuestion(Question.builder().question("?").answer(2.5).build());
        betSession.getPlayers().get(1).setGuessed(true);
        betSession.getPlayers().get(1).setGuess(1.5);
        betSession.getPlayers().get(1).setBetted(false);
        betSession.getPlayers().get(1).setCurrentBet(0);
        betSession.getPlayers().get(2).setGuessed(true);
        betSession.getPlayers().get(2).setGuess(1.5);
        betSession.getPlayers().get(2).setBetted(false);
        betSession.getPlayers().get(2).setCurrentBet(0);


        BetSessionEvaluationHelper.evaluateBetSessionIfNecessary(betSession);

        assertThat(betSession.isFinished(), is(false));
        assertThat(betSession.getPlayers().get(2).isWinner(),is(false));
        assertThat(betSession.getPlayers().get(0).isWinner(),is(false));
        assertThat(betSession.getPlayers().get(1).isWinner(),is(false));
    }

    @Test
    @DisplayName("BetSession where all Players where only 1 player remains not to have folded should be evaluated to be finished and the remaining player should be set as winner ")
    public void evaluationFoldTest(){
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        betSession.setQuestion(Question.builder().question("?").answer(2.5).build());
        betSession.getPlayers().get(1).setGuessed(true);
        betSession.getPlayers().get(1).setGuess(1.5);
        betSession.getPlayers().get(1).setBetted(true);
        betSession.getPlayers().get(1).setCurrentBet(0);
        betSession.getPlayers().get(1).setFolded(true);
        betSession.getPlayers().get(2).setGuessed(true);
        betSession.getPlayers().get(2).setGuess(50);
        betSession.getPlayers().get(2).setBetted(true);
        betSession.getPlayers().get(2).setCurrentBet(2);

        BetSessionEvaluationHelper.evaluateBetSessionIfNecessary(betSession);

        assertThat(betSession.isFinished(), is(true));
        assertThat(betSession.getPlayers().get(2).isWinner(),is(true));
        assertThat(betSession.getPlayers().get(0).isWinner(),is(false));
        assertThat(betSession.getPlayers().get(1).isWinner(),is(false));
    }

    @Test
    @DisplayName("BetSession where the non-dealing players have not folded, and their bets are different should not declare winner and the finished status should remain false")
    public void evaluationWithDifferentBetsTest(){
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        betSession.setQuestion(Question.builder().question("?").answer(2.5).build());
        betSession.getPlayers().get(1).setGuessed(true);
        betSession.getPlayers().get(1).setGuess(1.5);
        betSession.getPlayers().get(1).setBetted(true);
        betSession.getPlayers().get(1).setCurrentBet(0);
        betSession.getPlayers().get(2).setGuessed(true);
        betSession.getPlayers().get(2).setGuess(50);
        betSession.getPlayers().get(2).setBetted(true);
        betSession.getPlayers().get(2).setCurrentBet(2);

        BetSessionEvaluationHelper.evaluateBetSessionIfNecessary(betSession);

        assertThat(betSession.isFinished(), is(false));
        assertThat(betSession.getPlayers().get(2).isWinner(),is(false));
        assertThat(betSession.getPlayers().get(0).isWinner(),is(false));
        assertThat(betSession.getPlayers().get(1).isWinner(),is(false));
    }






    private BetSession getBetSessionWithThreePlayers(){
        BetSessionPlayer player1 = BetSessionPlayer.builder().id("1").name("Jane").cash(100).build();
        BetSessionPlayer player2 = BetSessionPlayer.builder().id("2").name("John").cash(100).build();
        BetSessionPlayer player3 = BetSessionPlayer.builder().id("3").name("Doe").cash(100).build();
        return BetSession.builder()
                .players(List.of(player1,player2,player3))
                .build();
    }
}