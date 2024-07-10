package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.entity.User;
import com.kafein.internshipdemo.enums.Role;
import com.kafein.internshipdemo.payload.request.LoginRequest;
import com.kafein.internshipdemo.payload.request.RegisterRequest;
import com.kafein.internshipdemo.payload.response.AuthenticationResponse;
import com.kafein.internshipdemo.payload.response.MessageResponse;
import com.kafein.internshipdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;



    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user);

        return ResponseEntity
                .ok()
                .body(new AuthenticationResponse(token,
                        user.getFirstName() + " " + user.getLastName(),
                        user.getRole().name()));
    }

    public ResponseEntity<AuthenticationResponse> login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()));

        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow();
        String token = jwtService.generateToken(user);

        return ResponseEntity
                .ok()
                .body(new AuthenticationResponse(token,
                        user.getFirstName() + " " + user.getLastName(),
                        user.getRole().name()));
    }
}
