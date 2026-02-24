package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.MeasurementPlanRequest;
import sk.uniza.fri.telemedicine.entities.MeasurementPlan;
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
    public MeasurementPlan createMeasurementPlan(@Valid @RequestBody MeasurementPlanRequest request) {
        return measurementPlanService.createMeasurementPlan(request);
    }

}
