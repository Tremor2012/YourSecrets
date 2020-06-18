package com.romaopera.mysecrets.service;

import com.romaopera.mysecrets.model.Role;
import com.romaopera.mysecrets.model.User;
import com.romaopera.mysecrets.repository.UserRepo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class UserService implements UserDetailsService {

    private final MailSender mailSender;
    private final UserRepo userRepo;
    private final SecreteService secreteService;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    public UserService(UserRepo userRepo, MailSender mailSender, SecreteService secreteService) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
        this.secreteService = secreteService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public User findByUserName(String userName) {
        return userRepo.findByUsername(userName);
    }

    public Optional<User> findById(Long id) {
        Optional<User> user = userRepo.findById(id);
        return user;
    }

    public boolean addUser(User user) {
        User userFromDB = userRepo.findByUsername(user.getUsername());
        if (userFromDB != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        sendMessage(user);
        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s!\n"
                            + "Welcome to Your Secrets! Please, visit next link: http://localhost:8080/activate/%S",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional
    public void deleteUser(Long id) {

        User u = userRepo.findById(id).get();
        secreteService.deleteUserSecrets(u);

        entityManager.clear();
        User user = entityManager.find(User.class, id);
        entityManager.persist(user);
        entityManager.remove(entityManager.contains(user) ? user : entityManager.merge(user));
        entityManager.flush();
        entityManager.clear();
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null)
            return false;

        user.setActivationCode(null);
        userRepo.save(user);

        return true;
    }

    public void saveUser(User user, String username, String password1, String password2, Map<String, String> model) {

        if (!StringUtils.isEmpty(password1) && password1.equals(password2))
            user.setPassword(passwordEncoder.encode(password1));

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : model.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(user);
    }

    public void updateProfile(User user, String password1, String password2, String email) {

        String userEmail = user.getEmail() != null ? user.getEmail() : " ";
        boolean isEmailChanged = (email != null && !email.equals(userEmail));

        if (isEmailChanged) {
            user.setEmail(email);
            if(!StringUtils.isEmpty(email))
                user.setActivationCode(UUID.randomUUID().toString());
        }

        if (!StringUtils.isEmpty(password1) && password1.equals(password2))
            user.setPassword(passwordEncoder.encode(password1));

        if (isEmailChanged)
            sendMessage(user);

        userRepo.save(user);
    }
}
