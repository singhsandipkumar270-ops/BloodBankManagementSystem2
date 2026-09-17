package com.example.bloodbankmanagementsystem2;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodRequestRepository
        extends JpaRepository<BloodRequest, Long> {

}