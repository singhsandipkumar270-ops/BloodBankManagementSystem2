package com.example.bloodbankmanagementsystem2;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BloodRequestController {

    private final BloodRequestRepository bloodRequestRepository;

    public BloodRequestController(
            BloodRequestRepository bloodRequestRepository) {

        this.bloodRequestRepository =
                bloodRequestRepository;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInUser") != null;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("role"));
    }

    // ==============================
    // BLOOD REQUEST FORM
    // ==============================

    @GetMapping("/blood-request")
    public String bloodRequestPage(
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        return "blood-request";
    }

    // ==============================
    // SUBMIT BLOOD REQUEST
    // ==============================

    @PostMapping("/blood-request")
    public String submitBloodRequest(
            @ModelAttribute BloodRequest bloodRequest,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        if (bloodRequest.getRequesterName() == null ||
                bloodRequest.getRequesterName().trim().isEmpty()) {

            return "redirect:/blood-request?error=name";
        }

        if (bloodRequest.getEmail() == null ||
                bloodRequest.getEmail().trim().isEmpty()) {

            return "redirect:/blood-request?error=email";
        }

        if (!isValidBloodGroup(
                bloodRequest.getBloodGroup())) {

            return "redirect:/blood-request?error=bloodGroup";
        }

        if (bloodRequest.getQuantity() < 1 ||
                bloodRequest.getQuantity() > 10) {

            return "redirect:/blood-request?error=quantity";
        }

        if (bloodRequest.getHospital() == null ||
                bloodRequest.getHospital().trim().isEmpty()) {

            return "redirect:/blood-request?error=hospital";
        }

        if (bloodRequest.getPhone() == null ||
                bloodRequest.getPhone().trim().isEmpty()) {

            return "redirect:/blood-request?error=phone";
        }

        bloodRequest.setRequesterName(
                bloodRequest.getRequesterName().trim()
        );

        bloodRequest.setEmail(
                bloodRequest.getEmail().trim()
        );

        bloodRequest.setBloodGroup(
                bloodRequest.getBloodGroup()
                        .trim()
                        .toUpperCase()
        );

        bloodRequest.setHospital(
                bloodRequest.getHospital().trim()
        );

        bloodRequest.setPhone(
                bloodRequest.getPhone().trim()
        );

        bloodRequest.setStatus("PENDING");

        bloodRequestRepository.save(bloodRequest);

        return "redirect:/blood-request?success=true";
    }

    // ==============================
    // VIEW ALL BLOOD REQUESTS
    // ==============================

    @GetMapping("/blood-requests")
    public String showBloodRequests(
            Model model,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        if (!isAdmin(session)) {
            return "redirect:/";
        }

        model.addAttribute(
                "bloodRequests",
                bloodRequestRepository.findAll()
        );

        return "blood-requests";
    }

    // ==============================
    // UPDATE REQUEST STATUS
    // ==============================

    @PostMapping("/blood-request/status")
    public String updateRequestStatus(
            @RequestParam("id") Long id,
            @RequestParam("status") String status,
            HttpSession session) {

        if (!isLoggedIn(session)) {
            return "redirect:/login";
        }

        if (!isAdmin(session)) {
            return "redirect:/";
        }

        BloodRequest request =
                bloodRequestRepository
                        .findById(id)
                        .orElse(null);

        if (request == null) {
            return "redirect:/blood-requests";
        }

        if (!status.equals("APPROVED") &&
                !status.equals("REJECTED") &&
                !status.equals("PENDING")) {

            return "redirect:/blood-requests";
        }

        request.setStatus(status);

        bloodRequestRepository.save(request);

        return "redirect:/blood-requests?success=true";
    }

    // ==============================
    // BLOOD GROUP VALIDATION
    // ==============================

    private boolean isValidBloodGroup(
            String bloodGroup) {

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