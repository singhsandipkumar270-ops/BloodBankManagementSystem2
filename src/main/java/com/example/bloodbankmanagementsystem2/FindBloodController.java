package com.example.bloodbankmanagementsystem2;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class FindBloodController {

    private final DonorRepository donorRepository;
    private final BloodStockRepository bloodStockRepository;

    public FindBloodController(
            DonorRepository donorRepository,
            BloodStockRepository bloodStockRepository) {

        this.donorRepository = donorRepository;
        this.bloodStockRepository = bloodStockRepository;
    }

    // ================= FIND BLOOD =================

    @GetMapping("/find-blood")
    public String findBlood(
            @RequestParam(required = false) String bloodGroup,
            Model model,
            HttpSession session) {

        // ================= CHECK LOGIN =================

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        // ================= SEARCH =================

        if (bloodGroup != null && !bloodGroup.trim().isEmpty()) {

            bloodGroup = bloodGroup.trim();

            // ================= FIND DONORS =================

            List<Donor> donors =
                    donorRepository.findByBloodGroup(bloodGroup);

            model.addAttribute(
                    "donors",
                    donors
            );

            // ================= FIND BLOOD STOCK =================

            Optional<BloodStock> stock =
                    bloodStockRepository.findByBloodGroup(bloodGroup);

            // ================= TOTAL UNITS =================

            int totalUnits = 0;

            if (stock.isPresent()) {

                totalUnits =
                        stock.get().getQuantity();
            }

            // ================= SEND DATA TO HTML =================

            model.addAttribute(
                    "selectedBloodGroup",
                    bloodGroup
            );

            model.addAttribute(
                    "bloodStock",
                    stock.orElse(null)
            );

            model.addAttribute(
                    "totalUnits",
                    totalUnits
            );
        }

        return "find-blood";
    }
}