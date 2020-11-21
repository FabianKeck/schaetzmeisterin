package de.fabiankeck.schaetzmeisterinbackendserver.Service;

import de.fabiankeck.schaetzmeisterinbackendserver.security.JwtUtils;
import de.fabiankeck.schaetzmeisterinbackendserver.utils.IdUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SignInService {

    private final JwtUtils jwtUtils;
    private final IdUtils idUtils;

    public SignInService(JwtUtils jwtUtils, IdUtils idUtils) {
        this.jwtUtils = jwtUtils;
        this.idUtils = idUtils;
    }

    public String signIn(String username){
        return jwtUtils.createToken(username ,new HashMap<>(Map.of("playerId",idUtils.createId())));
    }
}
