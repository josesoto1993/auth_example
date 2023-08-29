package com.auth.demo.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackendAliveController {
    @GetMapping("/ping")
    public String ping() {
        return "Backend ping returned!";
    }
}
