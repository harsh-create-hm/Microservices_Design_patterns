package com.example.servicea.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/service-a")
@RequiredArgsConstructor
public class ServiceAController {

    private final RestTemplate restTemplate;

    @GetMapping("/call")
    public Map<String, Object> callServiceB() {
        log.info("Service A: Received request, calling Service B");

        // traceId is automatically propagated via HTTP headers by Micrometer
        Map<?, ?> serviceBResponse = restTemplate.getForObject(
            "http://localhost:8081/service-b/info", Map.class);

        log.info("Service A: Got response from Service B");
        return Map.of(
            "serviceA", "processed",
            "serviceB", serviceBResponse,
            "message", "Trace propagated through both services - check logs for traceId"
        );
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        log.info("Service A: Info endpoint called");
        return Map.of("service", "service-a", "status", "UP");
    }
}
