package com.auth.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("authentication/login")
    public String login(@RequestBody UserLoginReq req) {
        return userService.authentication(req);
    }

    @GetMapping("/public-area/ping")
    public String pingPublicArea() {
        return "esta area es publica";
    }

    @GetMapping("/user-area/ping")
    public String pingUserArea() {
        return "esta area es de usuarios";
    }

    @GetMapping("/admin-area/ping")
    public String pingAdminArea() {
        return "esta area es de admins";
    }
}
