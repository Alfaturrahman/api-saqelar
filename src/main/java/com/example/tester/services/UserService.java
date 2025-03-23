package com.example.tester.services;

import com.example.tester.dto.AuthRequestDTO;
import com.example.tester.dto.AuthResponseDTO;
import com.example.tester.models.ApiResponse;
import com.example.tester.models.User;
import com.example.tester.repositories.UserRepository;
import com.example.tester.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ApiResponse registerUser(AuthRequestDTO request) {
        // Cek apakah email sudah ada
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return new ApiResponse(false, "Email already registered!");
        }

        // Cek panjang password minimal 6 karakter
        if (request.getPassword().length() < 6) {
            return new ApiResponse(false, "Password must be at least 6 characters long!");
        }

        // Cek apakah password dan konfirmasi password cocok
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new ApiResponse(false, "Passwords do not match!");
        }

        // Simpan user baru
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER"); // Set default role
        userRepository.save(user);

        return new ApiResponse(true, "Registration successful!");
    }

    public AuthResponseDTO loginUser(AuthRequestDTO request) {
        Optional<User> dbUser = userRepository.findByEmail(request.getEmail());
    
        if (dbUser.isEmpty() || !passwordEncoder.matches(request.getPassword(), dbUser.get().getPassword())) {
            return new AuthResponseDTO(null, "Invalid email or password", null, null);
        }
    
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            return new AuthResponseDTO(null, "Authentication Error", null, null);
        }
    
        // Generate JWT token
        String token = jwtUtil.generateToken(request.getEmail());
    
        // Ambil data user dari database
        User user = dbUser.get();
    
        // Kembalikan token + data user
        return new AuthResponseDTO(token, "Login Successful", user.getName(), user.getRole());
    }
}
