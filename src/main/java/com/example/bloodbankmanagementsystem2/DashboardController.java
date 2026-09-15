package com.example.bloodbankmanagementsystem2;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DashboardController {

    private final DonorRepository donorRepository;
    private final BloodStockRepository bloodStockRepository;

    public DashboardController(
            DonorRepository donorRepository,
            BloodStockRepository bloodStockRepository) {

        this.donorRepository = donorRepository;
        this.bloodStockRepository = bloodStockRepository;
    }

    // ================= DASHBOARD =================

    @GetMapping("/dashboard")
    public String dashboard(
            Model model,
            HttpSession session) {

        // ================= CHECK LOGIN =================

        if (session.getAttribute("loggedInUser") == null) {
            return "redirect:/login";
        }

        // ================= TOTAL DONORS =================

        long totalDonors = donorRepository.count();

        model.addAttribute(
                "totalDonors",
                totalDonors
        );

        // ================= BLOOD STOCK =================

        List<BloodStock> bloodStocks =
                bloodStockRepository.findAll();

        model.addAttribute(
                "bloodStocks",
                bloodStocks
        );

        // ================= TOTAL BLOOD UNITS =================

        long totalBloodUnits =
                bloodStocks.stream()
                        .mapToLong(BloodStock::getQuantity)
                        .sum();

        model.addAttribute(
                "totalBloodUnits",
                totalBloodUnits
        );

        // ================= BLOOD GROUP COUNT =================

        long bloodGroupCount =
                bloodStocks.stream()
                        .map(BloodStock::getBloodGroup)
                        .filter(group -> group != null && !group.isBlank())
                        .distinct()
                        .count();

        model.addAttribute(
                "bloodGroupCount",
                bloodGroupCount
        );

        // ================= BLOOD GROUP SUMMARY =================

        List<Donor> donors =
                donorRepository.findAll();

        Map<String, Long> bloodGroupSummary =
                donors.stream()
                        .filter(donor ->
                                donor.getBloodGroup() != null
                                        && !donor.getBloodGroup().isBlank())
                        .collect(
                                Collectors.groupingBy(
                                        Donor::getBloodGroup,
                                        LinkedHashMap::new,
                                        Collectors.counting()
                                )
                        );

        model.addAttribute(
                "bloodGroupSummary",
                bloodGroupSummary
        );

        // ================= USER NAME =================

        model.addAttribute(
                "userName",
                session.getAttribute("userName")
        );

        // ================= ROLE =================

        model.addAttribute(
                "role",
                session.getAttribute("role")
        );

        // ================= RETURN DASHBOARD =================

        return "dashboard";
    }
}