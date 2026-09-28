package com.college.bloodbank.service;

import com.college.bloodbank.entity.Donor;
import com.college.bloodbank.exception.BusinessException;
import com.college.bloodbank.exception.ResourceNotFoundException;
import com.college.bloodbank.repository.DonorRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class DonorService {
    private final DonorRepository repository;
    private final int minimumGapDays = 90;

    public DonorService(DonorRepository repository) { this.repository = repository; }
    public Donor create(Donor donor) {
        repository.findByEmail(donor.getEmail()).ifPresent(x -> { throw new BusinessException("A donor with this email already exists."); });
        return repository.save(donor);
    }
    public List<Donor> all() { return repository.findAll(); }
    public Donor get(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Donor not found with id: " + id)); }
    public Donor update(Long id, Donor input) {
        Donor donor = get(id);
        donor.setName(input.getName()); donor.setEmail(input.getEmail()); donor.setPhone(input.getPhone());
        donor.setBloodGroup(input.getBloodGroup()); donor.setDateOfBirth(input.getDateOfBirth()); donor.setActive(input.isActive());
        return repository.save(donor);
    }
    public void delete(Long id) { repository.delete(get(id)); }
    public boolean isEligible(Long id) {
        Donor donor = get(id);
        return donor.getLastDonationDate() == null || !donor.getLastDonationDate().plusDays(minimumGapDays).isAfter(LocalDate.now());
    }
    public int minimumGapDays() { return minimumGapDays; }
}
