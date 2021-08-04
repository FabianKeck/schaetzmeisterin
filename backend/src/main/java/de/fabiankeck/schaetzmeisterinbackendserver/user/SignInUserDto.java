package de.fabiankeck.schaetzmeisterinbackendserver.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInUserDto {
    private String name;
}
