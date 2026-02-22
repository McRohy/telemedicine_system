package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.PatientRequest;
import sk.uniza.fri.telemedicine.dto.PatientResponse;
import sk.uniza.fri.telemedicine.services.PatientService;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    public PatientResponse createPatient(@Valid @RequestBody PatientRequest request) {
        return patientService.createPatient(request);
    }

    @GetMapping("/{panNumber}")
    public List<PatientResponse> getPatientByPanNumber(@PathVariable Integer panNumber) {
        return patientService.getAllByDoctorsPanNumber(panNumber);
    }
}
