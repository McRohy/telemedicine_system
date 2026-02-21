package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
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
    public List<TypeOfMeasurement> getAllTypesOfMeasurement() {
        return typeOfMeasurementService.getAllTypesOfMeasurement();
    }

    @PostMapping
    public TypeOfMeasurement createTypeOfMeasurement(@Valid @RequestBody TypeOfMeasurement request) {
        return typeOfMeasurementService.createTypeOfMeasurement(request.getTypeName(), request.getUnits(),
                request.getMinValue(), request.getMaxValue());
    }
    

}
