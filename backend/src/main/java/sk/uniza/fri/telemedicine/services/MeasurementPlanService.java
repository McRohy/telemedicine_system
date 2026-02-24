package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.MeasurementPlanRequest;
import sk.uniza.fri.telemedicine.entities.MeasurementPlan;
import sk.uniza.fri.telemedicine.entities.MeasurementPlanTypes;
import sk.uniza.fri.telemedicine.enums.Frequency;
import sk.uniza.fri.telemedicine.repository.MeasurementPlanRepository;
import sk.uniza.fri.telemedicine.repository.MeasurementPlanTypesRepository;

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
    public MeasurementPlan createMeasurementPlan(MeasurementPlanRequest request) {

        MeasurementPlan plan = new MeasurementPlan();
        plan.setPatient(patientService.findByPersonalNumber(request.getPersonalNumber()));
        plan.setDoctor(doctorService.findByPanNumber(request.getPanNumber()));
        plan.setFrequency(Frequency.valueOf(request.getFrequency().toUpperCase()));
        plan.setTimeOfPlannedMeasurement(request.getTimeOfPlannedMeasurements());
        measurementPlanRepository.save(plan);

        request.getTypeOfMeasurementIds().forEach(t -> {
            MeasurementPlanTypes measurementPlanTypes = new MeasurementPlanTypes();
            measurementPlanTypes.setMeasurementPlan(plan);
            measurementPlanTypes.setTypeOfMeasurement(typeOfMeasurementService.findTypeOfMeasurementById(t));
            measurementPlanTypesRepository.save(measurementPlanTypes);
        });

        return plan;
    }
}
