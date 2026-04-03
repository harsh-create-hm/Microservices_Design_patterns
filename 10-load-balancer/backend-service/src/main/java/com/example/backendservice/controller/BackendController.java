package com.example.backendservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/backend")
public class BackendController {

    @Value("${server.port}")
    private int port;

    @GetMapping("/info")
    public Map<String, Object> info() throws UnknownHostException {
        String instanceId = InetAddress.getLocalHost().getHostName() + ":" + port;
        log.info("Backend instance {} handling request", instanceId);
        return Map.of(
            "instanceId", instanceId,
            "port", port,
            "message", "Response from backend instance on port " + port,
            "timestamp", System.currentTimeMillis()
        );
    }
}
