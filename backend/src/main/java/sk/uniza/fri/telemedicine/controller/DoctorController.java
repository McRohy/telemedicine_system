package sk.uniza.fri.telemedicine.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.DoctorRequest;
import sk.uniza.fri.telemedicine.dto.response.DoctorResponse;
import sk.uniza.fri.telemedicine.service.core.DoctorService;

/**
 * REST controller for doctor management.
 * Provides endpoints for doctor registration and view doctors.
 */
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public DoctorResponse createDoctor(@Valid @RequestBody DoctorRequest request) {
        return doctorService.createDoctor(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<DoctorResponse> getDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchLastName) {
        return doctorService.getDoctors(page, size, searchLastName);
    }
}

