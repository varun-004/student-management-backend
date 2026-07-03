package com.example.student_management_system.service;

import com.example.student_management_system.auth.AuthResponse;
import com.example.student_management_system.auth.LoginRequest;
import com.example.student_management_system.auth.RegisterRequest;

import com.example.student_management_system.entity.User;

import com.example.student_management_system.repository.UserRepository;

import com.example.student_management_system.security.CustomUserDetailsService;

import com.example.student_management_system.security.JwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.example.student_management_system.student.entity.Student;
import com.example.student_management_system.student.repository.StudentRepository;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService customUserDetailsService;

    private final StudentRepository studentRepository;

    // REGISTER
    public String register(RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );

        user.setRole(
                request.getRole()
        );

        User savedUser =
                userRepository.save(user);

        if (request.getRole().name()
                .equals("STUDENT")) {

            Student student =
                    Student.builder()
                            .name(
                                    request.getName()
                            )
                            .email(
                                    request.getEmail()
                            )
                            .build();

            studentRepository.save(student);
        }

        return "User Registered Successfully";
    }



    // LOGIN
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        UserDetails userDetails =
                customUserDetailsService
                        .loadUserByUsername(
                                request.getEmail()
                        );

        String token =
                jwtService.generateToken(userDetails);

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }

}