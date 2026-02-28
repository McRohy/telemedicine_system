package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.MeasurementPlanRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementPlanResponse;
import sk.uniza.fri.telemedicine.services.MeasurementPlanService;

@RestController
@RequestMapping("/api/plans")
public class MeasurementPlanController {

    private final MeasurementPlanService measurementPlanService;

    public MeasurementPlanController(MeasurementPlanService measurementPlanService) {
        this.measurementPlanService = measurementPlanService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MeasurementPlanResponse createMeasurementPlan(@Valid @RequestBody MeasurementPlanRequest request) {
        return measurementPlanService.createMeasurementPlan(request);
    }

    @GetMapping("/{personalNumber}")
    @ResponseStatus(HttpStatus.OK)
    public MeasurementPlanResponse getMeasurementPlanByPersonalNumber(@PathVariable String personalNumber) {
        return measurementPlanService.findMeasurementPlanByPersonalNumber(personalNumber);
    }
}
