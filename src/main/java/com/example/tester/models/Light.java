package com.example.tester.models;

import jakarta.persistence.*;

@Entity
@Table(name = "lights")
public class Light {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String location;
    
    @Column(nullable = false)
    private String status; // "ON" or "OFF"

    public Light() {}

    public Light(String location, String status) {
        this.location = location;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
