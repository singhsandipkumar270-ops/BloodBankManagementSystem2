package com.example.bloodbankmanagementsystem2;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public ForgotPasswordController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String checkEmail(
            @RequestParam("email") String email,
            Model model) {

        email = email.trim();

        User user = userRepository.findByEmail(email);

        if (user == null) {

            model.addAttribute(
                    "error",
                    "No account found with this email address."
            );

            return "forgot-password";
        }

        model.addAttribute(
                "email",
                email
        );

        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam("email") String email,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        email = email.trim();

        /* ================= PASSWORD MATCH ================= */

        if (!newPassword.equals(confirmPassword)) {

            model.addAttribute(
                    "email",
                    email
            );

            model.addAttribute(
                    "error",
                    "Passwords do not match."
            );

            return "reset-password";
        }


        /* ================= PASSWORD LENGTH ================= */

        if (newPassword.length() < 6) {

            model.addAttribute(
                    "email",
                    email
            );

            model.addAttribute(
                    "error",
                    "Password must be at least 6 characters."
            );

            return "reset-password";
        }


        /* ================= FIND USER ================= */

        User user =
                userRepository.findByEmail(email);

        if (user == null) {

            model.addAttribute(
                    "error",
                    "Account not found."
            );

            return "forgot-password";
        }


        /* ================= BCrypt PASSWORD ================= */

        String encryptedPassword =
                passwordEncoder.encode(newPassword);

        user.setPassword(encryptedPassword);

        userRepository.save(user);


        /* ================= SUCCESS ================= */

        return "redirect:/login?resetSuccess=true";
    }
}