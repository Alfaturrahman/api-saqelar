package com.example.tester.controllers;

import com.example.tester.models.Light;
import com.example.tester.services.LightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/lights")
public class LightController {
    @Autowired
    private LightService lightService;

    @GetMapping
    public ResponseEntity<List<Light>> getAllLights() {
        return ResponseEntity.ok(lightService.getAllLights());
    }

    @GetMapping("/{location}")
    public ResponseEntity<Map<String, String>> getLightByLocation(@PathVariable String location) {
        Optional<Light> light = lightService.getLightByLocation(location);
        if (light.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("location", light.get().getLocation());
            response.put("status", light.get().getStatus());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{location}")
    public ResponseEntity<Map<String, String>> updateLightStatus(@PathVariable String location, @RequestBody Light lightRequest) {
        String status = lightRequest.getStatus();

        if (!status.equalsIgnoreCase("ON") && !status.equalsIgnoreCase("OFF")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Status harus 'ON' atau 'OFF'");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Light updatedLight = lightService.updateLightStatus(location, status);
        if (updatedLight == null) {
            Map<String, String> notFoundResponse = new HashMap<>();
            notFoundResponse.put("message", "Lokasi '" + location + "' tidak ditemukan");
            return ResponseEntity.notFound().build();
        }

        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Status lampu di '" + location + "' berhasil diganti menjadi " + status);
        successResponse.put("status", updatedLight.getStatus());
        successResponse.put("location", updatedLight.getLocation());

        return ResponseEntity.ok(successResponse);
    }
}
