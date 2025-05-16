package com.example.citycutbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class ProjectSecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    //Dette er en kæde af sikkerhedsfiltre, som Spring Security bruger til at kontrollere adgang til forskellige URL'er.
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        //CSRF (Cross-Site Request Forgery) CSRF er en type sikkerhedssårbarhed, hvor en ondsindet hjemmeside eller email
        //kan få en brugers browser til at udføre uønskede handlinger på en anden side, hvor brugeren er autentificeret.
        // Jwt beskytter mod dette
        http
                .csrf(csrf -> csrf.disable()) // JWT beskytter mod CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Aktiverer CORS og bruger separat metode (se nedenstående)
                //til at definere reglerne
                //CORS (Cross-Origin Resource Sharing) er en browser-mekanisme, der kontrollerer, hvordan ressourcer kan deles mellem forskellige domæner.
                //Det er en sikkerhedsfunktion, der skal konfigureres korrekt for at tillade legitim kommunikation mellem domæner.
                .authorizeHttpRequests((request) -> request
                                .requestMatchers("/api/v1/user/test1", "/api/v1/user/register","/index.html","/api/v1/user/login",
                                        "/api/v1/treatments", "/api/v1/timeslots").permitAll() //dette betyder at alle kan tilgå disse sider uden login TODO: tilføj rette endpoints
                                .requestMatchers("/api/v1/treatments/add").hasRole("ADMIN") //TODO: tilføj rette endpoints
                                .requestMatchers("/api/v1/booking").hasAnyRole("ADMIN", "CUSTOMER") //TODO: tilføj rette endpoints
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        var obj = http.build(); //denne afslutter konfigurationen og opsætningen
        return obj;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:63342","https://localhost:8080", "http://127.0.0.1:8080")); // Tilladte domæner
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
