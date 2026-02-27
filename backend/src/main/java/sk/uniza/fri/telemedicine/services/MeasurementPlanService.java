package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.MeasurementPlanRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementPlanResponse;
import sk.uniza.fri.telemedicine.dto.response.MeasurementPlanTypesResponse;
import sk.uniza.fri.telemedicine.entities.*;
import sk.uniza.fri.telemedicine.enums.constrains.Frequency;
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
        Patient patient = patientService.findByPersonalNumber(request.getPersonalNumber());
        Doctor doctor = doctorService.findByPanNumber(request.getPanNumber());

        MeasurementPlan plan = mapToMeasurementPlan(request, patient, doctor);
        measurementPlanRepository.save(plan);

        List<MeasurementPlanTypes> planTypes = new ArrayList<>();
        request.getTypeOfMeasurementIds().forEach(t -> {
            TypeOfMeasurement type = typeOfMeasurementService.findTypeOfMeasurementById(t);
            MeasurementPlanTypes planType = new MeasurementPlanTypes();
            planType.setMeasurementPlan(plan);
            planType.setTypeOfMeasurement(type);
            planTypes.add(planType);
        });
        measurementPlanTypesRepository.saveAll(planTypes);

        return mapToMeasurementPlanResponse(plan, planTypes);
    }

    private MeasurementPlan mapToMeasurementPlan(MeasurementPlanRequest request, Patient patient, Doctor doctor) {
        MeasurementPlan plan = new MeasurementPlan();
        plan.setPatient(patient);
        plan.setDoctor(doctor);
        plan.setFrequency(Frequency.valueOf(request.getFrequency().toUpperCase()));
        plan.setTimeOfPlannedMeasurement(request.getTimeOfPlannedMeasurements());
        return plan;
    }

    private MeasurementPlanTypesResponse mapToMeasurementPlanTypesResponse(MeasurementPlan plan, MeasurementPlanTypes planType) {
        return new MeasurementPlanTypesResponse( plan.getPlanId(), planType.getTypeOfMeasurement().getTypeId(),  planType.getTypeOfMeasurement().getTypeName());
    }

    private MeasurementPlanResponse mapToMeasurementPlanResponse(MeasurementPlan plan, List<MeasurementPlanTypes> planTypes) {
        List<MeasurementPlanTypesResponse> typeResponses = planTypes.stream()
                .map(pt -> mapToMeasurementPlanTypesResponse(plan, pt))
                .toList();
        return new MeasurementPlanResponse(plan.getPlanId(), plan.getPatient().getPersonalNumber(), plan.getDoctor().getPanNumber(),
                plan.getTimeOfPlannedMeasurement().toString(), plan.getFrequency(), typeResponses);
    }
}
