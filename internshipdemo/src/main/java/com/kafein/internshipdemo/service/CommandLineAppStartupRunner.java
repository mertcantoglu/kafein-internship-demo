package com.kafein.internshipdemo.service;

import com.kafein.internshipdemo.payload.request.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.*;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    @Autowired
    AuthService authService;

    @Value("${spring.datasource.url}")
    private String url;

    public void run(String...args) throws Exception {

        System.out.println(url);
        RegisterRequest admin = RegisterRequest.builder()
                .email("admin@gmail.com")
                .password("admin123")
                .firstName("Admin")
                .lastName("System")
                .role("ADMIN")
                .build();
        authService.register(admin);
    }
}