package sk.uniza.fri.telemedicine.services.core;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.TypeOfMeasurementRequest;
import sk.uniza.fri.telemedicine.dto.response.TypeOfMeasurementResponse;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.TypeOfMeasurementRepository;

import java.util.List;

@Service
public class TypeOfMeasurementService {
    private final TypeOfMeasurementRepository typeOfMeasurementRepository;

    public TypeOfMeasurementService(TypeOfMeasurementRepository typeOfMeasurementRepository) {
        this.typeOfMeasurementRepository = typeOfMeasurementRepository;
    }

    public List<TypeOfMeasurementResponse> getTypesOfMeasurement() {
        return typeOfMeasurementRepository.findAll()
                .stream()
                .map(t -> mapToTypeOfMeasurementResponse(t))
                .toList();
    }

    public Page<TypeOfMeasurementResponse> getPagedTypesOfMeasurement(int page, int size, String searchTypeName) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("typeName").ascending());

        if (searchTypeName != null && !searchTypeName.isBlank()) {
            return typeOfMeasurementRepository.findByTypeNameStartingWithIgnoreCase(searchTypeName, pageable)
                    .map(type -> mapToTypeOfMeasurementResponse(type));
        }
        return typeOfMeasurementRepository.findAll(pageable).map(type -> mapToTypeOfMeasurementResponse(type));
    }

    @Transactional
    public TypeOfMeasurementResponse createTypeOfMeasurement(TypeOfMeasurementRequest request) {
        if (request.getMinValue() >= request.getMaxValue()) {
            throw new IllegalArgumentException("Min value must be less than max value");
        }
        if (typeOfMeasurementRepository.existsByTypeName(request.getTypeName())) {
            throw new DuplicateException("Type of measurement already exists");
        }
        TypeOfMeasurement typeOfMeasurement = mapToTypeOfMeasurement(request);
        typeOfMeasurementRepository.save(typeOfMeasurement);

        return mapToTypeOfMeasurementResponse(typeOfMeasurement);
    }

    public TypeOfMeasurement findTypeOfMeasurementById(Long id) {
        return typeOfMeasurementRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Type of measurement not found"));
    }

    private TypeOfMeasurement mapToTypeOfMeasurement(TypeOfMeasurementRequest request) {
        TypeOfMeasurement typeOfMeasurement = new TypeOfMeasurement();
        typeOfMeasurement.setTypeName(request.getTypeName());
        typeOfMeasurement.setUnits(request.getUnits());
        typeOfMeasurement.setMinValue(request.getMinValue());
        typeOfMeasurement.setMaxValue(request.getMaxValue());
        return typeOfMeasurement;
    }

    private TypeOfMeasurementResponse mapToTypeOfMeasurementResponse(TypeOfMeasurement typeOfMeasurement) {
        return new TypeOfMeasurementResponse(
                typeOfMeasurement.getTypeId(),
                typeOfMeasurement.getTypeName(),
                typeOfMeasurement.getUnits(),
                typeOfMeasurement.getMinValue(),
                typeOfMeasurement.getMaxValue()
        );
    }
}
