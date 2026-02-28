package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.TypeOfMeasurementRequest;
import sk.uniza.fri.telemedicine.dto.response.TypeOfMeasurementResponse;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;
import sk.uniza.fri.telemedicine.services.TypeOfMeasurementService;

import java.util.List;

@RestController
@RequestMapping("/api/types")
public class TypeOfMeasurementController {

    private final TypeOfMeasurementService typeOfMeasurementService;

    public TypeOfMeasurementController(TypeOfMeasurementService typeOfMeasurementService) {
        this.typeOfMeasurementService = typeOfMeasurementService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TypeOfMeasurementResponse> getAllTypesOfMeasurement() {
        return typeOfMeasurementService.getAllTypesOfMeasurement();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeOfMeasurementResponse createTypeOfMeasurement(@Valid @RequestBody TypeOfMeasurementRequest request) {
        return typeOfMeasurementService.createTypeOfMeasurement(request);
    }
}
