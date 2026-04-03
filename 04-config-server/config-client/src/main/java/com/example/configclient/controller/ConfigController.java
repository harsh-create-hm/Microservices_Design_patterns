package com.example.configclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RefreshScope
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Value("${app.message:No message configured}")
    private String message;

    @Value("${app.version:unknown}")
    private String version;

    @Value("${app.environment:unknown}")
    private String environment;

    @Value("${feature.enabled:false}")
    private boolean featureEnabled;

    @Value("${feature.max-retries:1}")
    private int maxRetries;

    @GetMapping
    public Map<String, Object> getConfig() {
        return Map.of(
            "message", message,
            "version", version,
            "environment", environment,
            "featureEnabled", featureEnabled,
            "maxRetries", maxRetries
        );
    }
}
