package com.college.bloodbank.service;

import com.college.bloodbank.entity.*;
import com.college.bloodbank.exception.BusinessException;
import com.college.bloodbank.repository.BloodUnitRepository;
import com.college.bloodbank.repository.IssueRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
public class IssueService {
    private final BloodUnitRepository unitRepository;
    private final IssueRecordRepository issueRepository;
    private final InventoryService inventoryService;

    public IssueService(BloodUnitRepository unitRepository, IssueRecordRepository issueRepository, InventoryService inventoryService) {
        this.unitRepository = unitRepository; this.issueRepository = issueRepository; this.inventoryService = inventoryService;
    }

    @Transactional
    public IssueRecord issue(IssueRecord request) {
        inventoryService.refreshStatuses();
        LocalDate cutoff = LocalDate.now().plusDays(7);
        List<BloodUnit> available = unitRepository.findByStatusAndBloodGroupAndExpiryDateAfterOrderByExpiryDateAsc(
                BloodUnitStatus.AVAILABLE, request.getBloodGroup(), cutoff);
        if (available.size() < request.getUnitsIssued()) {
            throw new BusinessException("Insufficient eligible stock. Units expiring within 7 days or already expired cannot be issued.");
        }
        request.setIssueDate(LocalDate.now());
        IssueRecord saved = issueRepository.save(request);
        for (int i = 0; i < request.getUnitsIssued(); i++) {
            BloodUnit unit = available.get(i);
            unit.setStatus(BloodUnitStatus.ISSUED);
            unit.setIssueRecord(saved);
            unitRepository.save(unit);
        }
        return saved;
    }

    public List<IssueRecord> all() { return issueRepository.findAll(); }
}
