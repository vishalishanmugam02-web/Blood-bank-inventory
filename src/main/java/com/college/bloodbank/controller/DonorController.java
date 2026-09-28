package com.college.bloodbank.controller;

import com.college.bloodbank.entity.Donor;
import com.college.bloodbank.service.DonorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donors")
public class DonorController {
    private final DonorService service;
    public DonorController(DonorService service) { this.service = service; }
    private void requireAdmin(HttpSession s) { if (!"ADMIN".equals(s.getAttribute("role"))) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN, "Admin login required."); }

    @PostMapping public Donor create(@Valid @RequestBody Donor donor, HttpSession s) { requireAdmin(s); return service.create(donor); }
    @GetMapping public List<Donor> all(HttpSession s) { requireAdmin(s); return service.all(); }
    @GetMapping("/{id}") public Donor get(@PathVariable Long id, HttpSession s) { requireAdmin(s); return service.get(id); }
    @PutMapping("/{id}") public Donor update(@PathVariable Long id, @Valid @RequestBody Donor donor, HttpSession s) { requireAdmin(s); return service.update(id, donor); }
    @DeleteMapping("/{id}") public Map<String,String> delete(@PathVariable Long id, HttpSession s) { requireAdmin(s); service.delete(id); return Map.of("message", "Donor deleted successfully"); }
    @GetMapping("/{id}/eligibility") public Map<String,Object> eligibility(@PathVariable Long id, HttpSession s) { requireAdmin(s); return Map.of("donorId", id, "eligible", service.isEligible(id), "minimumGapDays", service.minimumGapDays()); }
}
