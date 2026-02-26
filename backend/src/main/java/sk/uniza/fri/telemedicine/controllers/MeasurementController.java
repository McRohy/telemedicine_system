package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.MeasurementRecordRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementRecordResponse;
import sk.uniza.fri.telemedicine.services.MeasurementRecordService;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementController {

    private final MeasurementRecordService measurementRecordService;

    public MeasurementController(MeasurementRecordService measurementRecordService) {
        this.measurementRecordService = measurementRecordService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public MeasurementRecordResponse trackMeasurementRecord(@Valid @RequestBody MeasurementRecordRequest request) {
        return measurementRecordService.trackNewMeasurement(request);
    }

}
