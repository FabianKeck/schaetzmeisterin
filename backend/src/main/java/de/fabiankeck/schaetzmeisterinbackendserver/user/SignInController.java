package de.fabiankeck.schaetzmeisterinbackendserver.user;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signin")
public class SignInController {
    private final SignInService signInService;

    public SignInController(SignInService signInService) {
        this.signInService = signInService;
    }

    @GetMapping("/")
    public String signIn( @RequestParam String username){

        return signInService.signIn(username);
    }
}
