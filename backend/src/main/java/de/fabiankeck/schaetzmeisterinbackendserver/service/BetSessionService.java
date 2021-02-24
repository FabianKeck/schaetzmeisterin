package de.fabiankeck.schaetzmeisterinbackendserver.service;

import de.fabiankeck.schaetzmeisterinbackendserver.Handler.AskHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.Handler.FoldHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.Handler.GuessHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.Handler.PlaceBetHandler;
import de.fabiankeck.schaetzmeisterinbackendserver.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.nio.channels.spi.AbstractSelectionKey;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class BetSessionService {
    private final GuessHandler guessHandler = new GuessHandler();
    private final PlaceBetHandler placeBetHandler = new PlaceBetHandler();
    private final FoldHandler foldHandler = new FoldHandler();


    public void guess(BetSession betSession, String playerId, double guess) {
        guessHandler.handle(betSession, playerId, guess);
    }

    public void bet(BetSession betSession, String playerId, int betValue){
        placeBetHandler.handle(betSession, playerId, betValue);
    }

    public void fold(BetSession betSession, String playerId){
        foldHandler.handle(betSession,playerId,null);
    }
}
