package com.example.bloodbankmanagementsystem2;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class RegisterController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ================= REGISTER PAGE =================

    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    // ================= REGISTER USER =================

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {

        // Check email already exists
        if (userRepository.existsByEmail(user.getEmail())) {

            return "redirect:/register?error=true";
        }

        // Password ko encrypt karke database me save karna
        String encryptedPassword =
                passwordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        // Save user
        userRepository.save(user);

        // Registration successful
        return "redirect:/login?registered=true";
    }
}