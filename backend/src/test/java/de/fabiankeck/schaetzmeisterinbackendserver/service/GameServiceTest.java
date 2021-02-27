package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.Handler.AskHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.Handler.FoldHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.Handler.GuessHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.Handler.PlaceBetHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.GameDao;
import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.model.*;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;


class GameServiceTest {

    IdUtils idUtils = mock(IdUtils.class);
    SmUserDao userDao= mock(SmUserDao.class);
    GameDao gameDao = mock(GameDao.class);

    private final AskHandler askHandler = mock(AskHandler.class);
    private final GuessHandler guessHandler = mock(GuessHandler.class);
    private final PlaceBetHandler placeBetHandler = mock(PlaceBetHandler.class);
    private final FoldHandler foldHandler = mock(FoldHandler.class);

    GameService gameService = new GameService(
            gameDao,
            idUtils,
            userDao,
            askHandler,
            guessHandler,
            placeBetHandler,
            foldHandler
    );


    @Test
    @DisplayName("userSignIn(String) with emptyGameID should return a new Game Object and call IdUtils.createID")
    public void userSignInTest(){
        //given

        String gameId= "gameId";
        SmUser user = SmUser.builder()
                .id("123")
                .username("John")
                .build();
        when(idUtils.createId()).thenReturn(gameId);

        Game expected = Game.builder()
                .id( gameId)
                .players(List.of(Player.builder().id(user.getId()).name(user.getUsername()).build()))
                .build();

        //when

        when(userDao.findById(user.getId())).thenReturn(Optional.of(user));
        when(gameDao.save(expected)).thenReturn(expected);
        Game actual = gameService.userSignIn("123");
        //then
        assertThat(actual,is(expected));
        verify(idUtils).createId();
        verify(gameDao).save(expected);
    }
    @Test
    @DisplayName("userSignIn with GameId should return the updated game")
    public void userSignInTestWithId(){
        //given
        String gameId ="gameId";
        SmUser initialUser = SmUser.builder().id("123").username("Jane").build();
        SmUser userToAdd = SmUser.builder().id("456").username("John").build();
        when(idUtils.createId()).thenReturn(gameId);
        Game initial= Game.builder()
                .id(gameId)
                .players(new ArrayList<>(List.of(Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build())))
                .build();
        Game updated =  Game.builder()
                .id(gameId)
                .players(List.of(
                        Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build(),
                        Player.builder().id(userToAdd.getId()).name(userToAdd.getUsername()).build()
                ))
                .build();
        //when
        when(gameDao.findById(gameId)).thenReturn(Optional.of(initial));
        when(gameDao.save(updated)).thenReturn(updated);
        when(userDao.findById(userToAdd.getId())).thenReturn(Optional.of(userToAdd));
        Game actual = gameService.userSignIn(userToAdd.getId(), gameId);
        //then
        assertThat(actual.getId(),is(gameId));
        assertThat(actual.getPlayers().stream().map(Player::getId).collect(Collectors.toList()), containsInAnyOrder("123","456"));
        verify(gameDao).findById(gameId);
        verify(gameDao).save(updated);
    }
  @Test
    @DisplayName("userSignIn with invalid GameId should throw NotFound")
    public void signInWithInvalidId(){
        //when

      try {
          gameService.userSignIn("123","id");
          fail();
      } catch (Exception e) {
          assertThat(e.getMessage(),is(HttpStatus.NOT_FOUND.toString()));
      }
    }

    @Test
    @DisplayName("startGame with vaild userId should return updated Game")
    public void startGameTest(){
        String gameId ="gameId";
        SmUser initialUser = SmUser.builder().id("123").username("Jane").build();
        Game initial= Game.builder()
                .id(gameId)
                .players(List.of(Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build()))
                .build();
        when(gameDao.findById(gameId)).thenReturn(Optional.of(initial));

        //when
        Game actual = gameService.startGame(gameId, initialUser.getId());

        //then
        assertThat(actual.getId(), is(gameId));
        assertThat(actual.isStarted(),is(true));
        verify(gameDao).save(any());

    }
     @Test
    @DisplayName("startGame with invalid userId should throw ")
    public void startGameInvaildUser(){
        String gameId ="gameId";
        SmUser initialUser = SmUser.builder().id("123").username("Jane").build();
        Game initial= Game.builder()
                .id(gameId)
                .players(List.of(Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build()))
                .build();
        when(gameDao.findById(gameId)).thenReturn(Optional.of(initial));

        //when
        assertThrows(ResponseStatusException.class,()->gameService.startGame(gameId, "otherId"));
    }

    @Test
    @DisplayName("userSignIn with started game should throw")
    void signInStartedTest(){
        String gameId ="gameId";
        SmUser initialUser = SmUser.builder().id("123").username("Jane").build();
        Game game= Game.builder()
                .id(gameId)
                .players(List.of(Player.builder().id(initialUser.getId()).name(initialUser.getUsername()).build()))
                .started(true)
                .build();
        when(gameDao.findById(gameId)).thenReturn(Optional.of(game));

        //when
        assertThrows(ResponseStatusException.class, ()->gameService.userSignIn(initialUser.getId(), gameId));
        }

    @Test
    @DisplayName("Bet with valid user and correct bet should update game")
    public void BetWithValidUserTest(){
        //given
        String gameId ="gameId";
        int betValue = 2;
        Game initial = getGameWithThreeUsers(gameId);
        Game updated = getGameWithThreeUsers(gameId);
        updated.getBetSession().getPlayers().get(0).setCurrentBet(betValue);

        //when
        when(gameDao.findById(gameId)).thenReturn(Optional.of(initial));
        doAnswer(invocationOnMock -> {
            ((BetSession)invocationOnMock.getArgument(0)).getPlayers().get(0).setCurrentBet(betValue);
            return null;
        }).when(placeBetHandler).handle(initial.getBetSession(), initial.getPlayers().get(0).getId(),betValue);

        gameService.bet(gameId,initial.getPlayers().get(0).getId(), betValue);

        //then
        verify(gameDao).save(updated);
        verify(placeBetHandler).handle(initial.getBetSession(),initial.getPlayers().get(0).getId(),betValue);
    }

    @Test
    @DisplayName("Ask with valid user should load game, invoke askHandler.ask, save to DB and return updated game")
    public void AskWithValidUserTest(){
        //given
        String gameId ="gameId";
        Game initial = getGameWithThreeUsers(gameId);
        Game updated = getGameWithThreeUsers(gameId);
        Question question = Question.builder().question("question").answer(1).build();
        initial.getBetSession().getPlayers().get(0).setDealing(true);
           updated.getBetSession().getPlayers().get(0).setDealing(true);



        //when
        when(gameDao.findById(gameId)).thenReturn(Optional.of(initial));
        doAnswer(invocationOnMock -> {
            ((BetSession)invocationOnMock.getArgument(0)).setQuestion(question);
            return null;
        }).when(askHandler).handle(initial.getBetSession(), initial.getPlayers().get(0).getId(),question);

        gameService.ask(gameId,initial.getPlayers().get(0).getId(), question);
        //then

        updated.getBetSession().setQuestion(question);
        verify(gameDao).save(updated);
        verify(askHandler).handle(initial.getBetSession(), initial.getPlayers().get(0).getId(),question);
    }

    @Test
    @DisplayName("Guess with valid user should return updated Game and sve to DB")
    public void guessWithValidUserTest(){
        //given
        String gameId ="gameId";
        double guess= 2.5;
        Game initial = getGameWithThreeUsers(gameId);
        Game updated = getGameWithThreeUsers(gameId);
        Question question = Question.builder().question("question").answer(1).build();
        updated.getBetSession().getPlayers().get(1).setGuess(guess);

        //when
        when(gameDao.findById(gameId)).thenReturn(Optional.of(initial));
        doAnswer(invocationOnMock -> {
            ((BetSession)invocationOnMock.getArgument(0)).getPlayers().get(1).setGuess(guess);
            return null;
        }).when(guessHandler).handle(initial.getBetSession(), initial.getPlayers().get(1).getId(),guess);

        Game actual = gameService.guess(gameId,initial.getPlayers().get(1).getId(), guess);
        //then

        assertThat(actual,is(updated));
        verify(gameDao).save(updated);
        verify(guessHandler).handle(initial.getBetSession(), initial.getPlayers().get(1).getId(),guess);
    }

    @Test
    @DisplayName("Fold with valid user should return updated Game and sve to DB")
    public void foldWithValidUserTest(){
        //given
        String gameId ="gameId";
        double guess= 2.5;
        Game initial = getGameWithThreeUsers(gameId);
        Game updated = getGameWithThreeUsers(gameId);
        updated.getBetSession().getPlayers().get(1).setFolded(true);

        //when
        when(gameDao.findById(gameId)).thenReturn(Optional.of(initial));
        doAnswer(invocationOnMock -> {
            ((BetSession)invocationOnMock.getArgument(0)).getPlayers().get(1).setFolded(true);
            return null;
        }).when(foldHandler).handle(initial.getBetSession(), initial.getPlayers().get(1).getId());

        Game actual = gameService.fold(gameId,initial.getPlayers().get(1).getId());
        //then

        assertThat(actual,is(updated));
        verify(gameDao).save(updated);
        verify(foldHandler).handle(initial.getBetSession(), initial.getPlayers().get(1).getId());
    }



    private Game getGameWithThreeUsers(String gameId){
        BetSessionPlayer player1 = BetSessionPlayer.builder().id("1").name("Jane").cash(100).build();
        BetSessionPlayer player2 = BetSessionPlayer.builder().id("2").name("John").cash(100).build();
        BetSessionPlayer player3 = BetSessionPlayer.builder().id("3").name("Doe").cash(100).build();
        BetSession betSession = BetSession.builder()
                .players(List.of(player1,player2,player3))
                .build();
        return Game.builder()
                .betSession(betSession)
                .players(betSession.getPlayers().stream().map(betSessionPlayer -> Player.builder()
                        .name(betSessionPlayer.getName())
                        .id(betSessionPlayer.getId())
                        .build()
                ).collect(Collectors.toList()))
                .id(gameId)
                .build();
    }

}