package com.example.bloodbankmanagementsystem2;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class BloodStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bloodGroup;

    private int quantity;

    // ================= DEFAULT CONSTRUCTOR =================

    public BloodStock() {
    }

    // ================= CONSTRUCTOR =================

    public BloodStock(String bloodGroup, int quantity) {
        this.bloodGroup = bloodGroup;
        this.quantity = quantity;
    }

    // ================= GET ID =================

    public Long getId() {
        return id;
    }

    // ================= SET ID =================

    public void setId(Long id) {
        this.id = id;
    }

    // ================= GET BLOOD GROUP =================

    public String getBloodGroup() {
        return bloodGroup;
    }

    // ================= SET BLOOD GROUP =================

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    // ================= GET QUANTITY =================

    public int getQuantity() {
        return quantity;
    }

    // ================= SET QUANTITY =================

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}