package com.college.bloodbank.controller;

import com.college.bloodbank.entity.BloodUnit;
import com.college.bloodbank.entity.Donation;
import com.college.bloodbank.service.InventoryService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService service;
    public InventoryController(InventoryService service) { this.service = service; }
    private void requireAdmin(HttpSession s) { if (!"ADMIN".equals(s.getAttribute("role"))) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN, "Admin login required."); }

    @PostMapping("/donations") public Donation donate(@Valid @RequestBody Donation donation, HttpSession s) { requireAdmin(s); return service.logDonation(donation); }
    @GetMapping("/units") public List<BloodUnit> units(HttpSession s) { requireAdmin(s); return service.allUnits(); }
    @GetMapping("/stock") public Map<String,Long> stock(HttpSession s) { if (s.getAttribute("role") == null) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required."); return service.stock(); }
    @PostMapping("/refresh") public Map<String,String> refresh(HttpSession s) { requireAdmin(s); service.refreshStatuses(); return Map.of("message", "Inventory statuses refreshed"); }
}
