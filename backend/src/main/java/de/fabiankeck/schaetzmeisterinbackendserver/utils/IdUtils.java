package de.fabiankeck.schaetzmeisterinbackendserver.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdUtils {

    public String createId(){
        return UUID.randomUUID().toString();
    }
}
