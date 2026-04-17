package sk.uniza.fri.telemedicine.service.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.config.TextProvider;
import sk.uniza.fri.telemedicine.dto.request.TypeOfMeasurementRequest;
import sk.uniza.fri.telemedicine.dto.response.TypeOfMeasurementResponse;
import sk.uniza.fri.telemedicine.entity.TypeOfMeasurement;
import sk.uniza.fri.telemedicine.exception.BusinessRuleException;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.TypeOfMeasurementRepository;

import java.util.List;

/**
 * Service for managing types of measurements.
 */
@Service
public class TypeOfMeasurementService {
    private final TypeOfMeasurementRepository typeOfMeasurementRepository;
    private final TextProvider textProvider;

    public TypeOfMeasurementService(TypeOfMeasurementRepository typeOfMeasurementRepository, TextProvider textProvider) {
        this.typeOfMeasurementRepository = typeOfMeasurementRepository;
        this.textProvider = textProvider;
    }

    /**
     * Returns a list of all types of measurement.
     */
    public List<TypeOfMeasurementResponse> getTypesOfMeasurement() {
        return typeOfMeasurementRepository.findAll()
                .stream()
                .map(t -> mapToTypeOfMeasurementResponse(t))
                .toList();
    }

    /**
     * Returns a paginated list of types of measurement with optional type name search.
     */
    public Page<TypeOfMeasurementResponse> getPagedTypesOfMeasurement(int page, int size, String searchTypeName) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("typeName").ascending());

        if (searchTypeName != null && !searchTypeName.isBlank()) {
            return typeOfMeasurementRepository.findByTypeNameStartingWithIgnoreCase(searchTypeName, pageable)
                    .map(type -> mapToTypeOfMeasurementResponse(type));
        }
        return typeOfMeasurementRepository.findAll(pageable).map(type -> mapToTypeOfMeasurementResponse(type));
    }

    /**
     * Creates new type of measurement.
     * The type name must be unique and min value must be less than max value.
     */
    @Transactional
    public TypeOfMeasurementResponse createTypeOfMeasurement(TypeOfMeasurementRequest request) {
        if (request.getMinValue() >= request.getMaxValue()) {
            throw new BusinessRuleException(textProvider.get("error.typeOfMeasurement.invalidRange"));
        }
        if (typeOfMeasurementRepository.existsByTypeName(request.getTypeName())) {
            throw new DuplicateException(textProvider.get("error.typeOfMeasurement.duplicate"));
        }
        TypeOfMeasurement typeOfMeasurement = mapToTypeOfMeasurement(request);
        typeOfMeasurementRepository.save(typeOfMeasurement);

        return mapToTypeOfMeasurementResponse(typeOfMeasurement);
    }

    public TypeOfMeasurement getTypeOfMeasurementById(Long id) {
        return typeOfMeasurementRepository.findById(id).orElseThrow(
                () -> new NotFoundException(textProvider.get("error.typeOfMeasurement.notFound")));
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
