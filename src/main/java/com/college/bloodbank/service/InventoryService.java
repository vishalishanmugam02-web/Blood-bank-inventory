package com.college.bloodbank.service;

import com.college.bloodbank.entity.*;
import com.college.bloodbank.exception.BusinessException;
import com.college.bloodbank.exception.ResourceNotFoundException;
import com.college.bloodbank.repository.BloodUnitRepository;
import com.college.bloodbank.repository.DonationRepository;
import com.college.bloodbank.repository.DonorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
public class InventoryService {
    private final BloodUnitRepository unitRepository;
    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final DonorService donorService;

    public InventoryService(BloodUnitRepository unitRepository, DonationRepository donationRepository,
                            DonorRepository donorRepository, DonorService donorService) {
        this.unitRepository = unitRepository; this.donationRepository = donationRepository;
        this.donorRepository = donorRepository; this.donorService = donorService;
    }

    @Transactional
    public Donation logDonation(Donation donation) {
        if (donation.getCollectionDate() == null) donation.setCollectionDate(LocalDate.now());
        if (donation.getCollectionDate().isAfter(LocalDate.now())) throw new BusinessException("Collection date cannot be in the future.");
        if (!donation.getExpiryDate().isAfter(donation.getCollectionDate())) throw new BusinessException("Expiry date must be after collection date.");
        Donor donor = donorRepository.findById(donation.getDonor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + donation.getDonor().getId()));
        if (!donor.isActive()) throw new BusinessException("This donor is inactive.");
        if (!donorService.isEligible(donor.getId())) throw new BusinessException("Donation rejected. Donor has not completed the minimum 90-day gap.");
        if (donation.getBloodGroup() != donor.getBloodGroup()) throw new BusinessException("Donation blood group must match the donor blood group.");
        donation.setDonor(donor);
        Donation saved = donationRepository.save(donation);
        donor.setLastDonationDate(donation.getCollectionDate());
        donorRepository.save(donor);
        for (int i = 0; i < donation.getUnits(); i++) {
            BloodUnit unit = new BloodUnit();
            unit.setDonation(saved); unit.setBloodGroup(saved.getBloodGroup());
            unit.setCollectionDate(saved.getCollectionDate()); unit.setExpiryDate(saved.getExpiryDate());
            unit.setStatus(calculateStatus(saved.getExpiryDate()));
            unitRepository.save(unit);
        }
        return saved;
    }

    public List<BloodUnit> allUnits() { refreshStatuses(); return unitRepository.findAll(); }

    public Map<String, Long> stock() {
        refreshStatuses();
        Map<String, Long> result = new LinkedHashMap<>();
        for (BloodGroup group : BloodGroup.values()) {
            result.put(group.name(), unitRepository.countByBloodGroupAndStatus(group, BloodUnitStatus.AVAILABLE));
        }
        return result;
    }

    @Transactional
    public void refreshStatuses() {
        LocalDate today = LocalDate.now();
        for (BloodUnit unit : unitRepository.findByStatus(BloodUnitStatus.AVAILABLE)) {
            unit.setStatus(calculateStatus(unit.getExpiryDate()));
            unitRepository.save(unit);
        }
        for (BloodUnit unit : unitRepository.findExpiringSoon(BloodUnitStatus.EXPIRING_SOON, today, today.plusDays(7))) {
            unit.setStatus(calculateStatus(unit.getExpiryDate()));
            unitRepository.save(unit);
        }
        for (BloodUnit unit : unitRepository.findByStatusAndExpiryDateLessThanEqual(BloodUnitStatus.EXPIRING_SOON, today)) {
            unit.setStatus(BloodUnitStatus.EXPIRED); unitRepository.save(unit);
        }
    }

    private BloodUnitStatus calculateStatus(LocalDate expiry) {
        LocalDate today = LocalDate.now();
        if (!expiry.isAfter(today)) return BloodUnitStatus.EXPIRED;
        if (!expiry.isAfter(today.plusDays(7))) return BloodUnitStatus.EXPIRING_SOON;
        return BloodUnitStatus.AVAILABLE;
    }
}
