package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BetSessionServiceTest {
    private final BetSessionService betSessionService = new BetSessionService();

    @Test
    @DisplayName("Bet with valid parameters should update Players bet and move ActivePlayer Index by 1")
    public void betTestValid(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        int betValue =3;

        //when
        betSessionService.bet(betSession,betSession.getPlayers().get(0).getId(),betValue);

        //then
        assertThat(betSession.getPlayers().get(0).getCurrentBet(),is(betValue));
        assertThat(betSession.getPlayers().get(0).getCash(),is(100-betValue));
        assertThat(betSession.getActivePlayerIndex(),is(1));


    }

    @Test
    @DisplayName(" Bet with invalid user should throw and return unchanged Game")
    public void BetWithInvalidUserTest(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        int betValue =3;

        //when
        assertThrows(ResponseStatusException.class,()->betSessionService.bet(betSession,betSession.getPlayers().get(1).getId(), betValue));
        //then
        assertThat(betSession,is(getBetSessionWithThreePlayers()));
    }

    @Test
    @DisplayName(" Bet with too large betValue user should throw and return unchanged Game")
    public void BetWithTooLargeBetValue(){
        BetSession betSession = getBetSessionWithThreePlayers();
        int betValue =101;

        assertThrows(ResponseStatusException.class,()->betSessionService.bet(betSession,betSession.getPlayers().get(0).getId(), betValue));
        assertThat(betSession,is(getBetSessionWithThreePlayers()));
    }

    @Test
    @DisplayName(" Bet with too small betValue user should throw and return unchanged Game")
    public void BetWithTooSmallBetValue(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setCurrentBet(6);
        betSession.setActivePlayerIndex(1);
        int betValue =5;

        //when
        assertThrows(ResponseStatusException.class,()->betSessionService.bet(betSession,betSession.getPlayers().get(1).getId(), betValue));

        //then
        assertThat(betSession.getActivePlayerIndex(),is(1));
        assertThat(betSession.getPlayers().get(1).getCash(),is(100));
    }

   @Test
   @DisplayName("Fold should set the players fold status to true and advance activePlayerIndex, if player is active")
   public void FoldTest(){
       //given
       BetSession betSession = getBetSessionWithThreePlayers();

       //when
       betSessionService.fold(betSession,betSession.getPlayers().get(0).getId());

       //then
       assertThat(betSession.getActivePlayerIndex(),is(1));
       assertThat(betSession.getPlayers().get(0).isFolded(),is(true));
   }

   @Test
   @DisplayName("Fold should not change bat session and throw, when player is not active")
   public void FoldUnactivePlayerTest(){
       //given
       BetSession betSession = getBetSessionWithThreePlayers();

       //when
       assertThrows(ResponseStatusException.class,()->betSessionService.fold(betSession,betSession.getPlayers().get(1).getId()));

       //then
       assertThat(betSession.getActivePlayerIndex(),is(0));
       assertThat(betSession.getPlayers().get(0).isFolded(),is(false));
   }

   @Test
   @DisplayName("folded player should be skipped, when updating active player Index")
   public void skipsFoldedUserTest(){
      //given
      BetSession betSession = getBetSessionWithThreePlayers();
      betSession.getPlayers().get(1).setFolded(true);

      //when
       betSessionService.bet(betSession,betSession.getPlayers().get(0).getId(),1);

       //then
       assertThat(betSession.getActivePlayerIndex(),is(2));

  }
    @Test
    @DisplayName("Dealing player should be skipped, when updating active player Index")
    public void skipsDealingUserTest(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(1).setDealing(true);

        //when
        betSessionService.bet(betSession,betSession.getPlayers().get(0).getId(),1);

        //then
        assertThat(betSession.getActivePlayerIndex(),is(2));
    }

    @Test
    @DisplayName("ask should set question")
    public void askSetQuestionTest(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question expected= Question.builder().question("'Question").answer(2.5).build();

        //when
        betSessionService.ask(betSession,betSession.getPlayers().get(0).getId(),expected);
        Question actual = betSession.getQuestion();
        //then
        assertThat(actual.getAnswer(), is(expected.getAnswer()));
        assertThat(actual.getQuestion(), is(expected.getQuestion()));

    }
    @Test
    @DisplayName("Ask should throw if player is not dealing")
    public void askWithWrongPlayerTest(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question expected= Question.builder().question("'Question").answer(2.5).build();

        //when
        assertThrows(ResponseStatusException.class,()->betSessionService.ask(betSession,betSession.getPlayers().get(1).getId(),expected));
        //then
        assertThat(betSession.getQuestion(), is(nullValue()));
    }
    @Test
    @DisplayName("Ask should throw if question is already defined")
    public void askWithQuestionPlayedTest(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question firstQuestion= Question.builder().question("'Question").answer(2.5).build();
        betSession.setQuestion(firstQuestion);
        Question newQuestion = Question.builder().question("another Question").answer(0).build();


        //when
        assertThrows(ResponseStatusException.class,()->betSessionService.ask(betSession,betSession.getPlayers().get(0).getId(),newQuestion));
        //then
        assertThat(betSession.getQuestion(), is(firstQuestion));
    }

    @Test
    @DisplayName("guess with user who is not dealing should set playersGuess")
    public void guessSetGuessTest(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question question = Question.builder().question("'Question").answer(2.5).build();
        int guessingPlayerIndex=1;
        betSession.setQuestion(question);
        double guess = 2.5;

        //when
        betSessionService.guess(betSession,betSession.getPlayers().get(guessingPlayerIndex).getId(),guess);
        //then
        assertThat(betSession.getPlayers().get(guessingPlayerIndex).getGuess(), is(guess));
        assertThat(betSession.getPlayers().get(guessingPlayerIndex).isGuessed(), is(true));
    }

    @Test
    @DisplayName("guess with dealing User should throw")
    public void guessWithDealingUserTest(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question question = Question.builder().question("'Question").answer(2.5).build();
        int guessingPlayerIndex=0;
        betSession.setQuestion(question);
        double guess = 2.5;

        //when
        assertThrows(ResponseStatusException.class,()->betSessionService.guess(betSession,betSession.getPlayers().get(guessingPlayerIndex).getId(),guess));

        //then
        assertThat(betSession.getPlayers().get(guessingPlayerIndex).isGuessed(), is(false));
    }
    @Test
    @DisplayName("guess with guessed User should throw")
    public void guessWithGuessedUserTest(){
        //given
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question question = Question.builder().question("'Question").answer(2.5).build();
        int guessingPlayerIndex=1;
        betSession.setQuestion(question);
        betSession.getPlayers().get(guessingPlayerIndex).setGuessed(true);
        double initialGuess = 2.5;
        betSession.getPlayers().get(guessingPlayerIndex).setGuess(initialGuess);

        //when
        assertThrows(ResponseStatusException.class,()->betSessionService.guess(betSession,betSession.getPlayers().get(guessingPlayerIndex).getId(),3.));

        //then
        assertThat(betSession.getPlayers().get(guessingPlayerIndex).getGuess(), is(initialGuess));
    }

    @Test
    @DisplayName("Guess with nonparticipating userId should throw")
    public void guessWithNonparticipatingUserTest(){
        BetSession betSession = getBetSessionWithThreePlayers();
        betSession.getPlayers().get(0).setDealing(true);
        Question question = Question.builder().question("'Question").answer(2.5).build();
        betSession.setQuestion(question);

        assertThrows(ResponseStatusException.class,()->betSessionService.guess(betSession,"wrongId",3.));

    }

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

        betSessionService.evaluateBetSessionIfNecessary(betSession);

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


        betSessionService.evaluateBetSessionIfNecessary(betSession);

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

        betSessionService.evaluateBetSessionIfNecessary(betSession);

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

        betSessionService.evaluateBetSessionIfNecessary(betSession);

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