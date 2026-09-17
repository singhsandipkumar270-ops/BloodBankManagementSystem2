package com.example.bloodbankmanagementsystem2;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ================= LOGIN PAGE =================

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // ================= LOGIN =================

    @PostMapping("/login")
    public String loginUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session) {

        // ================= ADMIN LOGIN =================

        if (email.equals("admin@gmail.com")
                && password.equals("1234")) {

            session.setAttribute("loggedInUser", email);
            session.setAttribute("userName", "Admin");
            session.setAttribute("role", "ADMIN");

            return "redirect:/";
        }

        // ================= REGISTERED USER LOGIN =================

        User user = userRepository.findByEmail(email);

        if (user != null &&
                passwordEncoder.matches(password, user.getPassword())) {

            session.setAttribute("loggedInUser", user.getEmail());
            session.setAttribute("userName", user.getName());
            session.setAttribute("role", "USER");

            return "redirect:/";
        }

        // ================= WRONG LOGIN =================

        return "redirect:/login?error=true";
    }

    // ================= LOGOUT =================

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/?logout=true";
    }
}