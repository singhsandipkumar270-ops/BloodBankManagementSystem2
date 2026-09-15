package com.example.bloodbankmanagementsystem2;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DonorRepository extends JpaRepository<Donor, Long> {

    List<Donor> findByBloodGroup(String bloodGroup);

}