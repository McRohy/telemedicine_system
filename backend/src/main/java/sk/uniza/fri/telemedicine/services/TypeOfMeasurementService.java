package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.ResourceNotFoundException;
import sk.uniza.fri.telemedicine.repository.TypeOfMeasurementRepository;

import java.util.List;

@Service
public class TypeOfMeasurementService {
    private final TypeOfMeasurementRepository typeOfMeasurementRepository;

    public TypeOfMeasurementService(TypeOfMeasurementRepository typeOfMeasurementRepository) {
        this.typeOfMeasurementRepository = typeOfMeasurementRepository;
    }

    public List<TypeOfMeasurement> getAllTypesOfMeasurement() {
        return typeOfMeasurementRepository.findAll();
    }

    @Transactional
    public TypeOfMeasurement createTypeOfMeasurement(String typeName, String units, Integer minValue, Integer maxValue) {
       if (typeOfMeasurementRepository.existsByTypeName(typeName)){
           throw  new DuplicateException(("Type of measurement already exists"));
       }
        TypeOfMeasurement typeOfMeasurement = new TypeOfMeasurement();
        typeOfMeasurement.setTypeName(typeName);
        typeOfMeasurement.setUnits(units);
        typeOfMeasurement.setMinValue(minValue);
        typeOfMeasurement.setMaxValue(maxValue);
        return typeOfMeasurementRepository.save(typeOfMeasurement);
    }

    public TypeOfMeasurement findTypeOfMeasurementById(Integer id){
        return typeOfMeasurementRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Type of measurement with id : " + id + " not found"));
    }
}
