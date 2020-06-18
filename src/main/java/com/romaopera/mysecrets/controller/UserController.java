package com.romaopera.mysecrets.controller;

import com.romaopera.mysecrets.model.Role;
import com.romaopera.mysecrets.model.User;
import com.romaopera.mysecrets.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String UserList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "/userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userEdit(
            @RequestParam String username,
            @RequestParam String password1,
            @RequestParam String password2,
            @RequestParam Map<String, String> model,
            @RequestParam("id") User user
    ) {
        userService.saveUser(user, username, password1, password2, model);
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/user_delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){

        userService.deleteUser(id);
        return "redirect:/user";
    }

    @GetMapping("/profile")
    public String getProfileForm(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("password", user.getPassword());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password1,
            @RequestParam String password2,
            @RequestParam String email
    ){
        userService.updateProfile(user, password1, password2, email);
        return "redirect:/hello";
    }
}
