package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.model.SmUser;
import de.fabiankeck.schaetzmeisterinbackendserver.security.JwtUtils;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

class SignInServiceTest {


    JwtUtils jwtUtils = mock(JwtUtils.class);


    IdUtils idUtils = mock(IdUtils.class);

    SmUserDao userDao= mock(SmUserDao.class);


    SignInService signInService = new SignInService(userDao, jwtUtils, idUtils);


    @Test
    void SignIntest(){
        //given
        String username = "Jane";
        String expectedToken = "expectedToken" ;
        SmUser user = new SmUser("id","Jane");

        //when
        when(idUtils.createId()).thenReturn("id");
        when(jwtUtils.createToken(username,new HashMap<>(Map.of("playerId",idUtils.createId()))))
                .thenReturn(expectedToken);
        when(userDao.save( user)).thenReturn(user);
        String actual=  signInService.signIn(username);
        assertThat(actual, is(expectedToken));
        verify(userDao).save(user);

    }

}