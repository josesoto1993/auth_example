package com.auth.demo.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    public static final int TOKEN_VALID_MINUTES = 5;
    private final UserRepository userRepository;

    public String authentication(UserLoginReq req) {
        User user = userRepository.findByUsernameAndPassword(req.getUsername(), req.getPassword())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Acceso denegado: usuario o contraseña incorrectos"
                ));

        user.setToken(newTokenFrom(req.getUsername(), req.getPassword()));
        user.setTokenValidDate(LocalDateTime.now().plusMinutes(TOKEN_VALID_MINUTES));

        User savedUser = userRepository.save(user);

        return savedUser.getToken();
    }

    public boolean authorizationAdmin(String token) {
        Role userRole = getUserRole(token);

        return Role.ADMIN.equals(userRole);
    }

    public boolean authorizationUser(String token) {
        Role userRole = getUserRole(token);

        return Role.ADMIN.equals(userRole) || Role.USER.equals(userRole);
    }


    private String newTokenFrom(String username, String password) {
        // Esto debería ser nuevo token, recomendado standard JWT
        // Por tiempo, se coloca simplemente un string
        return "someGeneratedSuperSecretToken" + username + password;
    }

    private Role getUserRole(String token) {
        Optional<User> dbUser = userRepository.findByToken(token);

        User foundUser = dbUser
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Acceso denegado: Token no encontrado en la base de datos"
                ));

        if (foundUser.getTokenValidDate().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Acceso denegado: el token ha expirado");
        }

        return foundUser.getRole();
    }
}
