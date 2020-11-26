package de.fabiankeck.schaetzmeisterinbackendserver.security;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.security.auth.Subject;
import java.security.Principal;


@AllArgsConstructor
@Data
public class SignInUser implements Principal {
    public String id;
    private String name;


    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
