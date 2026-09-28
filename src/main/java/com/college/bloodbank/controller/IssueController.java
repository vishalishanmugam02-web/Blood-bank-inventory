package com.college.bloodbank.controller;

import com.college.bloodbank.entity.IssueRecord;
import com.college.bloodbank.service.IssueService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {
    private final IssueService service;
    public IssueController(IssueService service) { this.service = service; }
    private void requireLogin(HttpSession s) { if (s.getAttribute("role") == null) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required."); }
    private void requireAdmin(HttpSession s) { if (!"ADMIN".equals(s.getAttribute("role"))) throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN, "Admin login required."); }

    @PostMapping public IssueRecord issue(@Valid @RequestBody IssueRecord request, HttpSession s) {
        requireLogin(s);
        request.setRequestedBy((String)s.getAttribute("username"));
        return service.issue(request);
    }
    @GetMapping public List<IssueRecord> all(HttpSession s) { requireAdmin(s); return service.all(); }
}
