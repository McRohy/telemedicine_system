package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.MeasurementRecordRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementRecordResponse;
import sk.uniza.fri.telemedicine.services.core.MeasurementRecordService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/measurements")
public class MeasurementRecordController {

    private final MeasurementRecordService measurementRecordService;

    public MeasurementRecordController(MeasurementRecordService measurementRecordService) {
        this.measurementRecordService = measurementRecordService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('PATIENT')")
    public MeasurementRecordResponse createMeasurementRecord(@Valid @RequestBody MeasurementRecordRequest request) {
        return measurementRecordService.createMeasurementRecord(request);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public List<MeasurementRecordResponse> getMeasurementRecords(
            @RequestParam String personalNumber,
            @RequestParam Integer typeId,
            @RequestParam LocalDate period) {
        return measurementRecordService.getMeasurementRecords(personalNumber, typeId, period);
    }

    @GetMapping("/table")
    @PreAuthorize("hasAnyRole('DOCTOR', 'PATIENT')")
    public Page<MeasurementRecordResponse> getPagedMeasurementRecords(
            @RequestParam String personalNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer typeId) {
        return measurementRecordService.getPagedMeasurementRecords(personalNumber, page, size, typeId);
    }

}
