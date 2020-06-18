package com.romaopera.mysecrets.repository;

import com.romaopera.mysecrets.model.Secret;
import com.romaopera.mysecrets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface SecretsRepo extends JpaRepository<Secret, Long> {
    public Secret findByIndicator(String indicator);

    void deleteById(Long id);

    List<Secret> findAllByAuthor(User user);

    void deleteByAuthor(User user);


}
