package com.example.tester.repositories;

import com.example.tester.models.Light;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LightRepository extends JpaRepository<Light, Long> {
    Optional<Light> findByLocation(String location);
}
