package com.auth.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByToken(String token);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
