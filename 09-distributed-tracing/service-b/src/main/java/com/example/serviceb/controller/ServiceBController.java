package com.example.serviceb.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/service-b")
public class ServiceBController {

    @GetMapping("/info")
    public Map<String, String> info() {
        // The traceId from Service A is automatically carried here via HTTP headers
        log.info("Service B: Request received (same traceId as Service A)");
        return Map.of(
            "service", "service-b",
            "status", "UP",
            "message", "Same traceId propagated from Service A"
        );
    }
}
