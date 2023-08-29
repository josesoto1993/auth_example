package com.auth.demo.token;


import com.auth.demo.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final UserService userService;

    public void checkIfCanAccessUser(String token) {
        checkNullToken(token);
        if (!userService.authorizationUser(token)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Acceso denegado: Token no permite entrar a este path tipo user"
            );
        }
    }

    public void checkIfCanAccessAdmin(String token) {
        checkNullToken(token);
        if (!userService.authorizationAdmin(token)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Acceso denegado: Token no permite entrar a este path tipo admin"
            );
        }
    }

    private void checkNullToken(String token) {
        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Acceso denegado: Token null no permite entrar a este path"
            );
        }
    }
}
