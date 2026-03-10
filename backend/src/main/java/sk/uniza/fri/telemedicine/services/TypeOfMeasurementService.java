package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
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

    public List<TypeOfMeasurementResponse> getAllTypesOfMeasurement() {
        return typeOfMeasurementRepository.findAll()
                .stream()
                .map(t -> this.mapToTypeOfMeasurementResponse(t))
                .toList();
    }

    @Transactional
    public TypeOfMeasurementResponse createTypeOfMeasurement(TypeOfMeasurementRequest request) {
       if (typeOfMeasurementRepository.existsByTypeName(request.getTypeName())){
           throw  new DuplicateException(("Type of measurement already exists"));
       }
       TypeOfMeasurement typeOfMeasurement = this.mapToTypeOfMeasurement(request);
        typeOfMeasurementRepository.save(typeOfMeasurement);

       return this.mapToTypeOfMeasurementResponse(typeOfMeasurement);
    }

    @Transactional
    public TypeOfMeasurementResponse updateMinMaxTypeOfMeasurement(Integer id, TypeOfMeasurementRequest request) {
        TypeOfMeasurement typeOfMeasurement = typeOfMeasurementRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Type of measurement with id : " + id + " not found"));

        typeOfMeasurement.setMinValue(request.getMinValue());
        typeOfMeasurement.setMaxValue(request.getMaxValue());
        typeOfMeasurementRepository.save(typeOfMeasurement);

        return this.mapToTypeOfMeasurementResponse(typeOfMeasurement);
    }

    public TypeOfMeasurement findTypeOfMeasurementById(Integer id){
        return typeOfMeasurementRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Type of measurement with id : " + id + " not found"));
    }

    public TypeOfMeasurementResponse findTypeOfMeasurementByIdResponse(Integer id){
        return this.mapToTypeOfMeasurementResponse(findTypeOfMeasurementById(id));
    }

    private TypeOfMeasurement mapToTypeOfMeasurement(TypeOfMeasurementRequest request) {
        TypeOfMeasurement typeOfMeasurement = new TypeOfMeasurement();
        typeOfMeasurement.setTypeName(request.getTypeName());
        typeOfMeasurement.setUnits(request.getUnits());
        typeOfMeasurement.setMinValue(request.getMinValue());
        typeOfMeasurement.setMaxValue(request.getMaxValue());
        return typeOfMeasurement;
    }

    private  TypeOfMeasurementResponse mapToTypeOfMeasurementResponse(TypeOfMeasurement typeOfMeasurement) {
        return new TypeOfMeasurementResponse(typeOfMeasurement.getTypeId(), typeOfMeasurement.getTypeName(),
                typeOfMeasurement.getUnits(), typeOfMeasurement.getMinValue(), typeOfMeasurement.getMaxValue());
    }

}
