package com.romaopera.mysecrets.controller;

import com.romaopera.mysecrets.model.Secret;
import com.romaopera.mysecrets.model.User;
import com.romaopera.mysecrets.service.SecreteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MainController {
    private SecreteService secreteService;

    @Autowired
    public MainController(SecreteService secreteService){
        this.secreteService = secreteService;
    }

    @GetMapping(value = {"/hello", "/"})
    public String hello(){
        return "hello";
    }

    @GetMapping("/find")
    public String findAll(@AuthenticationPrincipal User user, Model model){
        List<Secret> secrets = secreteService.findBuUser(user);
        model.addAttribute("secrets", secrets);
        return "secretsList";
    }

    @GetMapping("/add")
    public String addSecretForm(Secret secret){
        return "addSecret";
    }

    @PostMapping("/add")
    public String addSecret(@RequestParam String indicator, @RequestParam String login, @RequestParam String password,
                            @AuthenticationPrincipal User user){
        Secret secret = new Secret(indicator, login, password, user);
        secreteService.saveSecret(secret);
        return "redirect:/find";
    }

    @GetMapping("/secret_delete/{id}")
    public String deleteSecret(@PathVariable("id") Long id){
        secreteService.deleteSecret(id);
        return "redirect:/find";
    }

    @GetMapping("/secret_update/{id}")
    public String updateSecretForm(@PathVariable("id") Long id, Model model){
        model.addAttribute(secreteService.findById(id));
        return "updateSecret";
    }

    @PostMapping("/secret_update")
    public String updateSecret(Secret secret, @AuthenticationPrincipal User user){
        secret.setAuthor(user);
        secreteService.saveSecret(secret);
        return "redirect:/find";
    }


}
