package com.example.tester.controllers;

import com.example.tester.dto.AuthRequestDTO;
import com.example.tester.dto.AuthResponseDTO;
import com.example.tester.models.ApiResponse;
import com.example.tester.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody AuthRequestDTO request) {
        ApiResponse response = userService.registerUser(request);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        AuthResponseDTO response = userService.loginUser(request);
        return response.getToken() != null ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
