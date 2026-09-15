package com.example.bloodbankmanagementsystem2;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final BloodStockRepository bloodStockRepository;

    public HomeController(BloodStockRepository bloodStockRepository) {
        this.bloodStockRepository = bloodStockRepository;
    }

    // ================= HOME PAGE =================

    @GetMapping("/")
    public String home(
            HttpSession session,
            Model model) {

        // ================= USER NAME =================

        String userName =
                (String) session.getAttribute("userName");

        if (userName != null) {

            model.addAttribute(
                    "userName",
                    userName
            );
        }

        // ================= ROLE =================

        String role =
                (String) session.getAttribute("role");

        if (role != null) {

            model.addAttribute(
                    "role",
                    role
            );
        }

        // ================= LOGIN STATUS =================

        boolean loggedIn =
                session.getAttribute("loggedInUser") != null;

        model.addAttribute(
                "loggedIn",
                loggedIn
        );

        // ================= BLOOD STOCK =================

        model.addAttribute(
                "bloodStocks",
                bloodStockRepository.findAll()
        );

        // ================= RETURN HOME =================

        return "home";
    }
}