package com.example.citycutbackend.user;

import com.example.citycutbackend.bookings.CustomerServiceImpl;
import com.example.citycutbackend.config.JwtService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Getter
@Setter
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private JwtService jwtService;
    private CustomerServiceImpl customerService;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService,CustomerServiceImpl customerService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.customerService = customerService;
    }

    public ResponseEntity<String> registerUser(UserModel user) {

        ResponseEntity response; //variabel som bliver brugt til at returnere et passende HTTP response
        try {
            Optional<UserModel> existingUser = userRepository.findByEmail(user.getEmail());

            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("En bruger med denne email findes allerede");
            }

            String hashPwd = passwordEncoder.encode(user.getPassword());//password bliver hashet (omdannet) så den ikke gemmes i klartekst
            user.setPassword(hashPwd);//den hashede password bliver sat på user

            UserModel savedUser = userRepository.save(user); //data gemmes i db
            customerService.createCustomerUser(savedUser); //Customer is created during Spring account creation

            if (savedUser.getId() > 0) {
                response = ResponseEntity.status(HttpStatus.CREATED)
                        .body("Bruger blev succesfuldt oprettet");
            } else {
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Bruger blev ikke oprettet");
            }
        } catch (Exception e) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Der opstod en fejl: " + e.getMessage());
        }

        return response;

    }

    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<UserModel> userOpt = userRepository.findByEmail(request.getEmail());
        if (userOpt.isPresent()) {
            UserModel user = userOpt.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                String token = jwtService.generateToken(user.getEmail(), user.getRole());
                return ResponseEntity.ok(new LoginResponse(token));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

}
