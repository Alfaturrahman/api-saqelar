package com.example.tester.services;

import com.example.tester.models.Light;
import com.example.tester.repositories.LightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LightService {
    @Autowired
    private LightRepository lightRepository;

    public List<Light> getAllLights() {
        return lightRepository.findAll();
    }

    public Optional<Light> getLightByLocation(String location) {
        return lightRepository.findByLocation(location);
    }

    public Light updateLightStatus(String location, String status) {
        Optional<Light> lightOpt = lightRepository.findByLocation(location);
        if (lightOpt.isPresent()) {
            Light light = lightOpt.get();
            light.setStatus(status);
            return lightRepository.save(light);
        }
        return null;
    }
}