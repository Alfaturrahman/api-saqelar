package com.example.tester.controllers;

import com.example.tester.models.User;
import com.example.tester.models.ApiResponse;
import com.example.tester.repositories.UserRepository;
import com.example.tester.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ApiResponse register(@RequestBody User user) {
        // Hash password dan simpan user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER"); // Role default
        userRepository.save(user);

        // Membuat objek MessageData untuk mengirimkan pesan sukses
        ApiResponse.MessageData messageData = new ApiResponse.MessageData("Registration Successful");

        // Mengembalikan respons dengan status sukses dan pesan dalam data
        return new ApiResponse(true, messageData);
    }


    @PostMapping("/login")
    public ApiResponse login(@RequestBody User user) {
        Optional<User> dbUser = userRepository.findByUsername(user.getUsername());

        if (dbUser.isEmpty()) {
            return new ApiResponse(false, "User Not Found");
        }

        // Cek password yang dikirim dan di database
        if (!passwordEncoder.matches(user.getPassword(), dbUser.get().getPassword())) {
            return new ApiResponse(false, "Incorrect Password");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            return new ApiResponse(false, "Authentication Error");
        }

        // Generate token
        String token = jwtUtil.generateToken(user.getUsername());

        // Membuat objek Data dengan token dan pesan
        ApiResponse.Data data = new ApiResponse.Data(token, "Login Successful");

        // Mengembalikan respons dengan status sukses dan data (token + message)
        return new ApiResponse(true, data);
    }


}
