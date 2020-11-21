package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.Service.SignInService;
import de.fabiankeck.schaetzmeisterinbackendserver.security.JwtUtils;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SignInServiceTest {


    JwtUtils jwtUtils = mock(JwtUtils.class);


    IdUtils idUtils = mock(IdUtils.class);


    SignInService signInService = new SignInService(jwtUtils, idUtils);


    @Test
    void SignIntest(){
        //given
        String username = "Jane";
        String expectedToken = "expectedToken" ;

        //when
        when(idUtils.createId()).thenReturn("id");
        when(jwtUtils.createToken(username,new HashMap<>(Map.of("playerId",idUtils.createId()))))
                .thenReturn(expectedToken);
        String actual=  signInService.signIn(username);
        assertThat(actual, is(expectedToken));

    }

}