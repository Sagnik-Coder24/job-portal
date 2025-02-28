package com.project.jobportal.controller;

import com.project.jobportal.entity.Users;
import com.project.jobportal.services.UserTypeService;
import com.project.jobportal.services.UsersService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class UsersController {

    public final UserTypeService userTypeService;
    public final UsersService usersService;

    @Autowired
    public UsersController(UserTypeService userTypeService, UsersService usersService) {
        this.userTypeService = userTypeService;
        this.usersService = usersService;
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("getAllTypes", userTypeService.getAll());
        model.addAttribute("user", new Users());
        return "register";
    }

    @PostMapping("/register/new")
    public String saveUser(@Valid @ModelAttribute Users user, BindingResult bindingResult, Model model) {

        Optional<Users> usersByEmail = usersService.getUsersByEmail(user.getEmail());

        if (usersByEmail.isPresent()) {
            model.addAttribute("emailError", "Email already registered, try to login.");
            model.addAttribute("getAllTypes", userTypeService.getAll());
            model.addAttribute("user", new Users());
            return "register";
        }

        usersService.addNew(user);
        return "redirect:/register/new";
    }

    @GetMapping("/register/new")
    public String dashboard() {
        return "dashboard";
    }

}
