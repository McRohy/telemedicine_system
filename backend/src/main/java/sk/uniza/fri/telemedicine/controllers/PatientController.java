package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.PatientRequest;
import sk.uniza.fri.telemedicine.dto.response.PatientResponse;
import sk.uniza.fri.telemedicine.services.core.PatientService;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public PatientResponse createPatient(@Valid @RequestBody PatientRequest request) {
        return patientService.createPatient(request);
    }

    @GetMapping("/{personalNumber}")
    @PreAuthorize("hasRole('DOCTOR')")
    public PatientResponse getPatientByPersonalNumber(@PathVariable String personalNumber) {
        return patientService.getPatientByPersonalNumber(personalNumber);
    }

    @GetMapping(params = "panNumber")
    @PreAuthorize("hasRole('DOCTOR')")
    public Page<PatientResponse> getPatientsByDoctorPanNumber(
            @RequestParam String panNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchLastName) {
        return patientService.getPatientsByDoctorPanNumber(panNumber, page, size, searchLastName);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<PatientResponse> getPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String searchLastName) {
        return patientService.getPatients(page, size, searchLastName);
    }
}
