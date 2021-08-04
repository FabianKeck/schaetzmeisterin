package de.fabiankeck.schaetzmeisterinbackendserver.user;

import de.fabiankeck.schaetzmeisterinbackendserver.user.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.user.SmUser;
import de.fabiankeck.schaetzmeisterinbackendserver.security.JwtUtils;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SignInService {

    private final SmUserDao userDao;
    private final JwtUtils jwtUtils;
    private final IdUtils idUtils;

    public SignInService(SmUserDao userDao, JwtUtils jwtUtils, IdUtils idUtils) {
        this.userDao = userDao;
        this.jwtUtils = jwtUtils;
        this.idUtils = idUtils;
    }

    public String signIn(String username){
        String randomId = idUtils.createId();
        userDao.save(new SmUser(randomId,username));
        return jwtUtils.createToken(username ,new HashMap<>(Map.of("playerId",randomId)));
    }
}