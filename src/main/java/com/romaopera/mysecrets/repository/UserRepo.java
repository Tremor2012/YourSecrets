package com.romaopera.mysecrets.repository;

import com.romaopera.mysecrets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    void deleteUserById(Long id);

    User findByActivationCode(String code);

//    Optional<User> findById(Long id);

}
