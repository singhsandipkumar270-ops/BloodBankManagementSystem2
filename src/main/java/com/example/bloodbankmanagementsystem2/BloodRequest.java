package com.example.bloodbankmanagementsystem2;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BloodRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requesterName;

    private String email;

    private String bloodGroup;

    private int quantity;

    private String hospital;

    private String phone;

    private String status;

    // ================= DEFAULT CONSTRUCTOR =================

    public BloodRequest() {
    }

    // ================= CONSTRUCTOR =================

    public BloodRequest(
            String requesterName,
            String email,
            String bloodGroup,
            int quantity,
            String hospital,
            String phone,
            String status) {

        this.requesterName = requesterName;
        this.email = email;
        this.bloodGroup = bloodGroup;
        this.quantity = quantity;
        this.hospital = hospital;
        this.phone = phone;
        this.status = status;
    }

    // ================= GETTERS AND SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}