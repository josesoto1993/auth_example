package com.auth.demo.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static org.apache.logging.log4j.util.Strings.EMPTY;


@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("authentication/login")
    public String login(@RequestBody UserLoginReq req) {
        try {
            return userService.authentication(req);
        } catch (Exception e) {
            log.error(e.getMessage());
            return EMPTY;
        }
    }

    @GetMapping("/public-area/ping")
    public String pingPublicArea() {
        try {
            return "esta area es publica";
        } catch (Exception e) {
            log.error(e.getMessage());
            return EMPTY;
        }
    }

    @GetMapping("/user-area/ping")
    public String pingUserArea() {
        try {
            return "esta area es de usuarios";
        } catch (Exception e) {
            log.error(e.getMessage());
            return EMPTY;
        }
    }

    @GetMapping("/admin-area/ping")
    public String pingAdminArea() {
        try {
            return "esta area es de admins";
        } catch (Exception e) {
            log.error(e.getMessage());
            return EMPTY;
        }
    }
}
