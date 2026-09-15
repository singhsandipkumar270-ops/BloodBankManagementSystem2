package com.example.bloodbankmanagementsystem2;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DonorController {

    private final DonorRepository donorRepository;

    public DonorController(DonorRepository donorRepository) {
        this.donorRepository = donorRepository;
    }


    // =====================================================
    // CHECK LOGIN
    // =====================================================

    private boolean isLoggedIn(HttpSession session) {

        return session.getAttribute("loggedInUser") != null;
    }


    // =====================================================
    // CHECK ADMIN
    // =====================================================

    private boolean isAdmin(HttpSession session) {

        return "ADMIN".equals(session.getAttribute("role"));
    }


    // =====================================================
    // DONOR REGISTRATION PAGE
    // =====================================================

    @GetMapping("/donor")
    public String donorPage(HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        return "donor";
    }


    // =====================================================
    // ALL DONORS PAGE
    // =====================================================

    @GetMapping("/donors")
    public String showDonors(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        model.addAttribute(
                "donors",
                donorRepository.findAll()
        );

        return "donors";
    }


    // =====================================================
    // REGISTER NEW DONOR
    // =====================================================

    @PostMapping("/donor")
    public String registerDonor(
            @ModelAttribute Donor donor,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }


        // =================================================
        // NAME VALIDATION
        // =================================================

        if (donor.getName() == null ||
                donor.getName().trim().isEmpty()) {

            return "redirect:/donor?error=name";
        }


        // =================================================
        // AGE VALIDATION
        // =================================================

        if (donor.getAge() < 18 ||
                donor.getAge() > 65) {

            return "redirect:/donor?error=age";
        }


        // =================================================
        // GENDER VALIDATION
        // =================================================

        if (donor.getGender() == null ||
                donor.getGender().trim().isEmpty()) {

            return "redirect:/donor?error=gender";
        }


        // =================================================
        // BLOOD GROUP VALIDATION
        // =================================================

        if (!isValidBloodGroup(donor.getBloodGroup())) {

            return "redirect:/donor?error=bloodGroup";
        }


        // =================================================
        // PHONE VALIDATION
        // =================================================

        if (donor.getPhone() == null ||
                donor.getPhone().trim().isEmpty()) {

            return "redirect:/donor?error=phone";
        }


        // =================================================
        // ADDRESS VALIDATION
        // =================================================

        if (donor.getAddress() == null ||
                donor.getAddress().trim().isEmpty()) {

            return "redirect:/donor?error=address";
        }


        // =================================================
        // CLEAN DATA
        // =================================================

        donor.setName(
                donor.getName().trim()
        );

        donor.setGender(
                donor.getGender().trim()
        );

        donor.setBloodGroup(
                donor.getBloodGroup().trim().toUpperCase()
        );

        donor.setPhone(
                donor.getPhone().trim()
        );

        donor.setAddress(
                donor.getAddress().trim()
        );


        // =================================================
        // SAVE DONOR
        // =================================================

        donorRepository.save(donor);


        // =================================================
        // SUCCESS
        // =================================================

        return "redirect:/?success=true";
    }


    // =====================================================
    // EDIT DONOR PAGE
    // ONLY ADMIN
    // =====================================================

    @GetMapping("/donor/edit/{id}")
    public String editDonor(
            @PathVariable Long id,
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }


        if (!isAdmin(session)) {
            return "redirect:/donors";
        }


        Donor donor = donorRepository
                .findById(id)
                .orElse(null);


        if (donor == null) {
            return "redirect:/donors";
        }


        model.addAttribute(
                "donor",
                donor
        );


        return "edit-donor";
    }


    // =====================================================
    // UPDATE DONOR
    // ONLY ADMIN
    // =====================================================

    @PostMapping("/donor/update")
    public String updateDonor(
            @ModelAttribute Donor donor,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }


        if (!isAdmin(session)) {
            return "redirect:/donors";
        }


        // =================================================
        // NAME VALIDATION
        // =================================================

        if (donor.getName() == null ||
                donor.getName().trim().isEmpty()) {

            return "redirect:/donor/edit/"
                    + donor.getId()
                    + "?error=name";
        }


        // =================================================
        // AGE VALIDATION
        // =================================================

        if (donor.getAge() < 18 ||
                donor.getAge() > 65) {

            return "redirect:/donor/edit/"
                    + donor.getId()
                    + "?error=age";
        }


        // =================================================
        // GENDER VALIDATION
        // =================================================

        if (donor.getGender() == null ||
                donor.getGender().trim().isEmpty()) {

            return "redirect:/donor/edit/"
                    + donor.getId()
                    + "?error=gender";
        }


        // =================================================
        // BLOOD GROUP VALIDATION
        // =================================================

        if (!isValidBloodGroup(donor.getBloodGroup())) {

            return "redirect:/donor/edit/"
                    + donor.getId()
                    + "?error=bloodGroup";
        }


        // =================================================
        // PHONE VALIDATION
        // =================================================

        if (donor.getPhone() == null ||
                donor.getPhone().trim().isEmpty()) {

            return "redirect:/donor/edit/"
                    + donor.getId()
                    + "?error=phone";
        }


        // =================================================
        // ADDRESS VALIDATION
        // =================================================

        if (donor.getAddress() == null ||
                donor.getAddress().trim().isEmpty()) {

            return "redirect:/donor/edit/"
                    + donor.getId()
                    + "?error=address";
        }


        // =================================================
        // CLEAN DATA
        // =================================================

        donor.setName(
                donor.getName().trim()
        );

        donor.setGender(
                donor.getGender().trim()
        );

        donor.setBloodGroup(
                donor.getBloodGroup().trim().toUpperCase()
        );

        donor.setPhone(
                donor.getPhone().trim()
        );

        donor.setAddress(
                donor.getAddress().trim()
        );


        // =================================================
        // UPDATE DATABASE
        // =================================================

        donorRepository.save(donor);


        // =================================================
        // SUCCESS
        // =================================================

        return "redirect:/donors?success=updated";
    }


    // =====================================================
    // DELETE DONOR
    // ONLY ADMIN
    // =====================================================

    @GetMapping("/donor/delete/{id}")
    public String deleteDonor(
            @PathVariable Long id,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }


        if (!isAdmin(session)) {
            return "redirect:/donors";
        }


        if (donorRepository.existsById(id)) {

            donorRepository.deleteById(id);
        }


        return "redirect:/donors?success=deleted";
    }


    // =====================================================
    // BLOOD GROUP VALIDATION METHOD
    // =====================================================

    private boolean isValidBloodGroup(String bloodGroup) {

        if (bloodGroup == null) {
            return false;
        }


        return bloodGroup.equals("A+") ||
                bloodGroup.equals("A-") ||
                bloodGroup.equals("B+") ||
                bloodGroup.equals("B-") ||
                bloodGroup.equals("O+") ||
                bloodGroup.equals("O-") ||
                bloodGroup.equals("AB+") ||
                bloodGroup.equals("AB-");
    }

}