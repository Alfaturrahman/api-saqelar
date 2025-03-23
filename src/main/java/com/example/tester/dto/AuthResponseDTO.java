package com.example.tester.dto;

public class AuthResponseDTO {
    private String token;
    private String message;
    private String name;
    private String role;

    public AuthResponseDTO(String token, String message, String name, String role) {
        this.token = token;
        this.message = message;
        this.name = name;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
