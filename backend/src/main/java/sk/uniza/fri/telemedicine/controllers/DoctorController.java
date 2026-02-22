package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.DoctorRequest;
import sk.uniza.fri.telemedicine.dto.DoctorResponse;
import sk.uniza.fri.telemedicine.entities.Doctor;
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
    public Doctor createdoctor(@Valid @RequestBody DoctorRequest request) {
        return doctorService.createDoctor(request);
    }

    @GetMapping
    public List<DoctorResponse> getAllDoctors() {
        return doctorService.getAllDoctors();
    }
}
