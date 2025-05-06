package com.example.citycutbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class ProjectSecurityConfig {

    //Dette er en kæde af sikkerhedsfiltre, som Spring Security bruger til at kontrollere adgang til forskellige URL'er.
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf");
        //CSRF (Cross-Site Request Forgery) CSRF er en type sikkerhedssårbarhed, hvor en ondsindet hjemmeside eller email
        //kan få en brugers browser til at udføre uønskede handlinger på en anden side, hvor brugeren er autentificeret.
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf((csrf) -> csrf
                        .csrfTokenRequestHandler(requestHandler)
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) //denne sætter CSFR-token til at blive gemt i en cookie,
                        //som kan læses på clientsiden, dvs. den kan bruges og hentes med js og inkluderes i HTTP-requests
                        .ignoringRequestMatchers("/contact", "/register")) //"offentlige" Endpoint som udelades for beskyttelse. Andre endpoints som kræver
                //login og som ændrer noget POST, PUT, DELETE er vigtige at få inkluderet i beskyttelse
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Aktiverer CORS og bruger separat metode (se nedenstående)
                //til at definere reglerne
                //CORS (Cross-Origin Resource Sharing) er en browser-mekanisme, der kontrollerer, hvordan ressourcer kan deles mellem forskellige domæner.
                //Det er en sikkerhedsfunktion, der skal konfigureres korrekt for at tillade legitim kommunikation mellem domæner.
                //.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((request) -> request
                        .requestMatchers("/test1").permitAll() //dette betyder at alle kan tilgå denne side uden login TODO: tilføj rette endpoints
                        .requestMatchers("/test3").hasRole("ADMIN") //her skal man være logget ind og have den rette rolle, og ellers TODO: tilføj rette endpoints
                        //vil man blive dirigeret til login side
                        .requestMatchers("/test2").hasAnyRole("ADMIN", "CUSTOMER") //TODO: tilføj rette endpoints
                );
        http.formLogin(Customizer.withDefaults()); //Disse to aktiverer en standard login-side og basic authentification
        http.httpBasic(Customizer.withDefaults());
        var obj = http.build(); //denne afslutter konfigurationen og opsætningen
        return obj;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://myfrontend.com")); // Tilladte domæner
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Tilladte metoder
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Tilladte headers
        configuration.setAllowCredentials(true); // Tillad credentials (f.eks. cookies, autorisation)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Anvend CORS på alle endpoints

        return source;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12); //default er 10 i styrke
        //Hvis jeg returnerede PasswordEncoderFactories.createDelegatingPasswordEncoder(); så kunne jeg vælge andre standarder men default er BCrypt
    }
}
