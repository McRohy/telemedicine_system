package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.MeasurementPlanRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementPlanResponse;
import sk.uniza.fri.telemedicine.services.MeasurementPlanService;

@RestController
@RequestMapping("/api/measurement-plans")
public class MeasurementPlanController {

    private final MeasurementPlanService measurementPlanService;

    public MeasurementPlanController(MeasurementPlanService measurementPlanService) {
        this.measurementPlanService = measurementPlanService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('DOCTOR')")
    public MeasurementPlanResponse createMeasurementPlan(@Valid @RequestBody MeasurementPlanRequest request) {
        return measurementPlanService.createMeasurementPlan(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DOCTOR')")
    public MeasurementPlanResponse updateMeasurementPlan(@PathVariable Integer id, @Valid @RequestBody MeasurementPlanRequest request) {
        return measurementPlanService.updateMeasurementPlan(id, request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public MeasurementPlanResponse getMeasurementPlanByPersonalNumber(@RequestParam String personalNumber) {
        return measurementPlanService.findMeasurementPlanByPersonalNumber(personalNumber);
    }
}
