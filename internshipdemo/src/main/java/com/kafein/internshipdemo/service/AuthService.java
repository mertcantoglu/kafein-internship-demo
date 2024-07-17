package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.dto.UserDTO;
import com.kafein.internshipdemo.entity.Employee;
import com.kafein.internshipdemo.entity.Leave;
import com.kafein.internshipdemo.entity.User;
import com.kafein.internshipdemo.enums.Role;
import com.kafein.internshipdemo.payload.request.LoginRequest;
import com.kafein.internshipdemo.payload.request.RegisterRequest;
import com.kafein.internshipdemo.payload.response.AuthenticationResponse;
import com.kafein.internshipdemo.payload.response.MessageResponse;
import com.kafein.internshipdemo.repository.EmployeeRepository;
import com.kafein.internshipdemo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${employee.break.duration}")
    Double breakDuration;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;




    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Role userRole = Role.valueOf(registerRequest.getRole().toUpperCase());
        User user;

        if (userRole == Role.EMPLOYEE){
          Employee employee = new Employee();
            employee.setFirstName(registerRequest.getFirstName());
            employee.setLastName(registerRequest.getLastName());
            employee.setEmail(registerRequest.getEmail());
            employee.setNumDaysBreak(breakDuration);
            employee.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            employee.setRole(userRole);
            employee.setDepartment(registerRequest.getDepartment());
            employeeRepository.save(employee);
            user = employee;
      }
        else {
        user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(userRole)
                .build();
        userRepository.save(user);
        }

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


    @Secured("ADMIN")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(),user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole()))
                .collect(Collectors.toList());
    }

    @Secured("ADMIN")
    public ResponseEntity<MessageResponse> deleteUser(int userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getRole().equals(Role.ADMIN)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Admin cannot be deleted!"));
        }

        userRepository.deleteById(userId);
        return ResponseEntity.ok(new MessageResponse("User deleted successfully"));
    }
 }
