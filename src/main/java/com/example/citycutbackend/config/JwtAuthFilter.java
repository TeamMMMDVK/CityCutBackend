package com.example.citycutbackend.config;

import com.example.citycutbackend.user.UserModel;
import com.example.citycutbackend.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

//denne klasse / filter fanger hver request og tjekker om der er gyldig jwt med
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            logger.info(token);
            if (jwtService.isTokenValid(token)) {
                String email = jwtService.extractUsername(token);
                Optional<UserModel> userOpt = userRepository.findByEmail(email);
                if (userOpt.isPresent()) {
                    UserModel user = userOpt.get();

                    logger.info("User found with email: {}", email);
                    logger.info("User role: {}", user.getRole());

                    List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole()));

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, null, authorities);

                    logger.debug("Authentication token created: {}", authToken);

                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    logger.info("Authentication set in SecurityContext");
                } else {
                    logger.warn("No user found with email: {}", email);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}

