package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.DoctorRequest;
import sk.uniza.fri.telemedicine.dto.response.DoctorResponse;
import sk.uniza.fri.telemedicine.services.DoctorService;
import java.util.List;

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
    public List<DoctorResponse> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
}

