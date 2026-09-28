package com.college.bloodbank.repository;
import com.college.bloodbank.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DonationRepository extends JpaRepository<Donation, Long> {}
