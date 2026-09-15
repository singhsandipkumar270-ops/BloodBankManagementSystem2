package com.example.bloodbankmanagementsystem2;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BloodStockController {

    private final BloodStockRepository bloodStockRepository;

    public BloodStockController(
            BloodStockRepository bloodStockRepository) {

        this.bloodStockRepository = bloodStockRepository;
    }

    // ================= CHECK LOGIN =================

    private boolean isLoggedIn(HttpSession session) {

        return session.getAttribute("loggedInUser") != null;
    }


    // ================= CHECK ADMIN =================

    private boolean isAdmin(HttpSession session) {

        return "ADMIN".equals(
                session.getAttribute("role")
        );
    }


    // ================= BLOOD STOCK PAGE =================

    @GetMapping("/blood-stock")
    public String bloodStockPage(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {

            return "redirect:/login";
        }

        model.addAttribute(
                "bloodStocks",
                bloodStockRepository.findAll()
        );

        model.addAttribute(
                "isAdmin",
                isAdmin(session)
        );

        return "blood-stock";
    }


    // ================= ADD STOCK PAGE =================

    @GetMapping("/blood-stock/add")
    public String addStockPage(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {

            return "redirect:/login";
        }

        if (!isAdmin(session)) {

            return "redirect:/blood-stock";
        }

        model.addAttribute(
                "bloodStock",
                new BloodStock()
        );

        return "add-blood-stock";
    }


    // ================= ADD STOCK =================

    @PostMapping("/blood-stock/add")
    public String addBloodStock(
            @RequestParam String bloodGroup,
            @RequestParam int quantity,
            HttpSession session) {

        if (!isLoggedIn(session)) {

            return "redirect:/login";
        }

        if (!isAdmin(session)) {

            return "redirect:/blood-stock";
        }


        // ================= QUANTITY VALIDATION =================

        if (quantity <= 0) {

            return "redirect:/blood-stock?error=quantity";
        }


        // ================= BLOOD GROUP VALIDATION =================

        if (bloodGroup == null ||
                bloodGroup.trim().isEmpty()) {

            return "redirect:/blood-stock?error=bloodGroup";
        }


        String cleanBloodGroup =
                bloodGroup.trim().toUpperCase();


        // ================= EXISTING STOCK =================

        BloodStock existingStock =
                bloodStockRepository
                        .findByBloodGroup(cleanBloodGroup)
                        .orElse(null);


        if (existingStock != null) {

            // Existing blood group
            // mein quantity add hogi

            existingStock.setQuantity(
                    existingStock.getQuantity()
                            + quantity
            );

            bloodStockRepository.save(
                    existingStock
            );

        } else {

            // ================= NEW BLOOD GROUP =================

            BloodStock newStock =
                    new BloodStock(
                            cleanBloodGroup,
                            quantity
                    );

            bloodStockRepository.save(
                    newStock
            );
        }


        return "redirect:/blood-stock?success=added";
    }


    // ================= EDIT STOCK PAGE =================

    @GetMapping("/blood-stock/edit/{id}")
    public String editBloodStock(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {

            return "redirect:/login";
        }

        if (!isAdmin(session)) {

            return "redirect:/blood-stock";
        }


        BloodStock bloodStock =
                bloodStockRepository
                        .findById(id)
                        .orElse(null);


        if (bloodStock == null) {

            return "redirect:/blood-stock";
        }


        model.addAttribute(
                "bloodStock",
                bloodStock
        );


        return "edit-blood-stock";
    }


    // ================= UPDATE STOCK =================

    @PostMapping("/blood-stock/update")
    public String updateBloodStock(
            @ModelAttribute BloodStock bloodStock,
            HttpSession session) {

        if (!isLoggedIn(session)) {

            return "redirect:/login";
        }

        if (!isAdmin(session)) {

            return "redirect:/blood-stock";
        }


        // ================= QUANTITY VALIDATION =================

        if (bloodStock.getQuantity() < 0) {

            return "redirect:/blood-stock?error=quantity";
        }


        // ================= UPDATE =================

        bloodStockRepository.save(
                bloodStock
        );


        return "redirect:/blood-stock?success=updated";
    }


    // ================= DELETE STOCK =================

    @GetMapping("/blood-stock/delete/{id}")
    public String deleteBloodStock(
            @PathVariable Long id,
            HttpSession session) {

        if (!isLoggedIn(session)) {

            return "redirect:/login";
        }

        if (!isAdmin(session)) {

            return "redirect:/blood-stock";
        }


        // ================= CHECK ID =================

        if (bloodStockRepository.existsById(id)) {

            bloodStockRepository.deleteById(id);
        }


        return "redirect:/blood-stock?success=deleted";
    }
}