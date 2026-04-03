package com.example.clientservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
public class ClientController {

    private final RestTemplate restTemplate;
    private int callCount = 0;

    @GetMapping("/call")
    public Map<String, Object> callBackend() {
        callCount++;
        log.info("Client: Making load-balanced call #{} to backend-service", callCount);

        // LoadBalancer resolves "backend-service" to one of the registered instances
        Map<?, ?> response = restTemplate.getForObject(
            "http://backend-service/backend/info", Map.class);

        return Map.of(
            "callNumber", callCount,
            "backendResponse", response,
            "message", "Load balancer distributed this call automatically"
        );
    }
}
