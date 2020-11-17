package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.GameService;
import de.fabiankeck.schaetzmeisterinbackendserver.dto.SignInUserDto;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Game;
import de.fabiankeck.schaetzmeisterinbackendserver.model.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


class GameServiceTest {



    GameService gameService = new GameService();

    @Test
    @DisplayName("userSignIn should Retrun a matching user Object")
    public void userSignInTest(){
        //given
        SignInUserDto signInUserDto = new SignInUserDto("Fabian");

        //when
        Game actual = gameService.initNewGame(signInUserDto);
        Game expected = new Game(List.of(new Player("Fabian")));
        //then
        assertThat(actual,is(expected));

    }

}