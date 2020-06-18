package com.romaopera.mysecrets.controller;

import com.romaopera.mysecrets.model.Role;
import com.romaopera.mysecrets.model.User;
import com.romaopera.mysecrets.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Collections;

@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addNewUser(User user, Model model){

        if(user.getUsername().length()==0 || user.getPassword().length()==0) {
            model.addAttribute("message", "Empty slot!");
            return "registration";
        }

        if(!userService.addUser(user)) {
            model.addAttribute("message", "User is exists!");
            return "registration";
        }

        return "redirect:/hello";
    }
    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if(isActivated){
            model.addAttribute("message", "User successfully activated");
        }else {
            model.addAttribute("message", "Activation code id not found!");
        }

        return "/login";
    }
}
