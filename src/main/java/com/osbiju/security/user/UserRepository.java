package com.osbiju.security.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    //we need to create this method to findByEmail
    Optional<User> findByEmail(String email);

}
