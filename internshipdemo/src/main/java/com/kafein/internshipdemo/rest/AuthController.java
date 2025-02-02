package com.kafein.internshipdemo.rest;

import com.kafein.internshipdemo.dto.UserDTO;
import com.kafein.internshipdemo.entity.User;
import com.kafein.internshipdemo.enums.Role;
import com.kafein.internshipdemo.payload.request.LoginRequest;
import com.kafein.internshipdemo.payload.request.RegisterRequest;
import com.kafein.internshipdemo.payload.response.AuthenticationResponse;
import com.kafein.internshipdemo.payload.response.MessageResponse;
import com.kafein.internshipdemo.service.AuthService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        return authService.register(registerRequest);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody LoginRequest loginRequest){
        return authService.login(loginRequest);

    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers(){
        return authService.getAllUsers();
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable int userId){
        return authService.deleteUser(userId);
    }
}
