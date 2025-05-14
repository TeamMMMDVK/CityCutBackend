package com.example.citycutbackend.config;

import com.example.citycutbackend.user.UserModel;
import com.example.citycutbackend.user.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Med denne klasse vil vi selv lave og styre autentificeringen af brugere

@Getter
@Setter
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public UserAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();//i denne variabel gemmes username fra Authentication objektet
        String pwd = authentication.getCredentials().toString(); //i denne variabel gemmes adgangskoden fra objektet
        Optional<UserModel> user = null;
        try {
            user = userRepository.findByEmail(username);
        } catch (Exception ex) {
            System.out.println("Database fejl =" + ex.getMessage());
        }
        if (user.isPresent()) {
            if (passwordEncoder.matches(pwd, user.get().getPassword())) {
                List<GrantedAuthority> authorities = new ArrayList<>();//Hvis adgangskoderne matcher, så oprettes en liste
                //med de autoriteter/roller som brugeren har
                authorities.add(new SimpleGrantedAuthority(user.get().getRole()));
                return new UsernamePasswordAuthenticationToken(username, pwd, authorities);
                //returnerer et nyt Authentication objekt med username, password og liste med roller
            } else {
                throw new BadCredentialsException("Invalid password");
            }
        } else {
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    //denne metode afgør om denne AutheticationProvider kan håndtere den givne Authentication type.
    //Dvs. Authentication objektet der gives som parameter skal være en instans af eller subklasse af
    //UsernamePasswordAuthenticationToken
    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
    //Når en autentificeringsanmodning modtages, vil Spring Security gå igennem alle konfigurerede
    //AuthenticationProvidere og kalde deres supports metode.
    //Den første AuthenticationProvider, der returnerer true fra supports metoden, vil blive brugt
    //til at håndtere autentificeringen.
}
