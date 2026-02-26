package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.MeasurementPlanRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementPlanResponse;
import sk.uniza.fri.telemedicine.entities.MeasurementPlan;
import sk.uniza.fri.telemedicine.entities.MeasurementPlanTypes;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;
import sk.uniza.fri.telemedicine.enums.Frequency;
import sk.uniza.fri.telemedicine.repository.MeasurementPlanRepository;
import sk.uniza.fri.telemedicine.repository.MeasurementPlanTypesRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class MeasurementPlanService {

    private final MeasurementPlanRepository measurementPlanRepository;
    private final MeasurementPlanTypesRepository measurementPlanTypesRepository;

    private final TypeOfMeasurementService typeOfMeasurementService;
    private final DoctorService doctorService;
    private final PatientService patientService;


    public MeasurementPlanService(MeasurementPlanRepository measurementPlanRepository,
                                  TypeOfMeasurementService typeOfMeasurementService,
                                  MeasurementPlanTypesRepository measurementPlanTypesRepository,
                                  DoctorService doctorService, PatientService patientService) {
        this.measurementPlanRepository = measurementPlanRepository;
        this.typeOfMeasurementService = typeOfMeasurementService;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.measurementPlanTypesRepository = measurementPlanTypesRepository;
    }

    @Transactional
    public MeasurementPlanResponse createMeasurementPlan(MeasurementPlanRequest request) {
        MeasurementPlan plan = new MeasurementPlan();
        plan.setPatient(patientService.findByPersonalNumber(request.getPersonalNumber()));
        plan.setDoctor(doctorService.findByPanNumber(request.getPanNumber()));
        plan.setFrequency(Frequency.valueOf(request.getFrequency().toUpperCase()));
        plan.setTimeOfPlannedMeasurement(request.getTimeOfPlannedMeasurements());
        measurementPlanRepository.save(plan);

        List<String> typeNames = new ArrayList<>();
        request.getTypeOfMeasurementIds().forEach(t -> {
            TypeOfMeasurement type = typeOfMeasurementService.findTypeOfMeasurementById(t);
            MeasurementPlanTypes measurementPlanTypes = new MeasurementPlanTypes();
            measurementPlanTypes.setMeasurementPlan(plan);
            measurementPlanTypes.setTypeOfMeasurement(type);
            measurementPlanTypesRepository.save(measurementPlanTypes);
            typeNames.add(type.getTypeName());
        });

        return new MeasurementPlanResponse(plan.getPlanId(), request.getPersonalNumber(),
                plan.getTimeOfPlannedMeasurement().toString(), plan.getFrequency(), typeNames);
    }
}
