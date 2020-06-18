package com.romaopera.mysecrets.service;

import com.romaopera.mysecrets.model.Secret;
import com.romaopera.mysecrets.model.User;
import com.romaopera.mysecrets.repository.SecretsRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class SecreteService {
    private final SecretsRepo secretsRepo;


    public SecreteService(SecretsRepo secretsRepo) {
        this.secretsRepo = secretsRepo;
    }

    public List<Secret> findAll(){
        return secretsRepo.findAll();
    }

    public List<Secret> findBuUser(User user){
        return secretsRepo.findAllByAuthor(user);
    }

    public Secret findById(Long id){
        return secretsRepo.getOne(id);
    }

    public Secret saveSecret(Secret secret){
        return secretsRepo.save(secret);
    }

    public void deleteSecret(Long id){
        secretsRepo.deleteById(id);
    }

    public void deleteUserSecrets(User user){
        secretsRepo.deleteByAuthor(user);
    }

}
