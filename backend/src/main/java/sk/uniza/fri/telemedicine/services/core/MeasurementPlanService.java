package sk.uniza.fri.telemedicine.services.core;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.MeasurementPlanRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementPlanResponse;
import sk.uniza.fri.telemedicine.dto.response.MeasurementPlanTypesResponse;
import sk.uniza.fri.telemedicine.entities.*;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.MeasurementPlanRepository;
import sk.uniza.fri.telemedicine.repository.MeasurementPlanTypesRepository;
import sk.uniza.fri.telemedicine.repository.MeasurementTimeRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MeasurementPlanService {

    private final MeasurementPlanRepository measurementPlanRepository;
    private final MeasurementPlanTypesRepository measurementPlanTypesRepository;
    private final MeasurementTimeRepository measurementTimeRepository;

    private final TypeOfMeasurementService typeOfMeasurementService;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public MeasurementPlanService(MeasurementPlanRepository measurementPlanRepository,
                                  TypeOfMeasurementService typeOfMeasurementService, MeasurementPlanTypesRepository measurementPlanTypesRepository,
                                  DoctorService doctorService, PatientService patientService, MeasurementTimeRepository measurementTimeRepository) {
        this.measurementPlanRepository = measurementPlanRepository;
        this.typeOfMeasurementService = typeOfMeasurementService;
        this.measurementPlanTypesRepository = measurementPlanTypesRepository;
        this.measurementTimeRepository = measurementTimeRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @Transactional
    public MeasurementPlanResponse createMeasurementPlan(MeasurementPlanRequest request) {
        Patient patient = patientService.findByPersonalNumber(request.getPersonalNumber());
        Doctor doctor = doctorService.findByPanNumber(request.getPanNumber());

        MeasurementPlan plan = mapToMeasurementPlan(request, patient, doctor);
        plan.setCreatedAt(LocalDateTime.now());
        plan.setLastUpdateAt(LocalDateTime.now());
        measurementPlanRepository.save(plan);

        List<MeasurementTime> measurementTimes = new ArrayList<>();
        for(LocalTime time : request.getTimesOfPlannedMeasurements()){
            MeasurementTime mt = new MeasurementTime();
            mt.setPlan(plan);
            mt.setTime(time);
            measurementTimeRepository.save(mt);
        }

        List<MeasurementPlanTypes> activeTypes = new ArrayList<>();
        for (Integer typeId : request.getTypeOfMeasurementIds()) {
            TypeOfMeasurement type = typeOfMeasurementService.findTypeOfMeasurementById(typeId);
            MeasurementPlanTypes planType = new MeasurementPlanTypes();
            planType.setMeasurementPlan(plan);
            planType.setTypeOfMeasurement(type);
            measurementPlanTypesRepository.save(planType);
        }

        return mapToMeasurementPlanResponse(plan, activeTypes, measurementTimes);
    }


    @Transactional
    public MeasurementPlanResponse updateMeasurementPlan(Integer id, MeasurementPlanRequest request) {
        MeasurementPlan plan = measurementPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Measurement plan not found"));

        plan.setFrequency(request.getFrequency());
        plan.setLastUpdateAt(LocalDateTime.now());
        measurementPlanRepository.save(plan);

        //hard delete
        measurementTimeRepository.deleteAllByPlanPlanId(plan.getPlanId());
        List<MeasurementTime> newTimes = new ArrayList<>();
        for (LocalTime time : request.getTimesOfPlannedMeasurements()) {
            MeasurementTime mt = new MeasurementTime();
            mt.setPlan(plan);
            mt.setTime(time);
            newTimes.add(mt);
        }
        measurementTimeRepository.saveAll(newTimes);

        // soft delete
        List<MeasurementPlanTypes> existingTypes = measurementPlanTypesRepository.findAllActiveTypesByMeasurementPlanId(plan.getPlanId());
        existingTypes.forEach(t -> t.setArchivedAt(LocalDateTime.now()));
        measurementPlanTypesRepository.saveAll(existingTypes);

        List<MeasurementPlanTypes> activeTypes = new ArrayList<>();
        for (Integer typeId : request.getTypeOfMeasurementIds()) {
            boolean existsAlready = false;
            for (MeasurementPlanTypes existing : existingTypes) {
                if (existing.getTypeOfMeasurement().getTypeId().equals(typeId)) {
                    existing.setArchivedAt(null); // reactivate
                    activeTypes.add(existing);
                    existsAlready = true;
                    break;
                }
            }
            if (!existsAlready) {
                TypeOfMeasurement type = typeOfMeasurementService.findTypeOfMeasurementById(typeId);
                MeasurementPlanTypes planType = new MeasurementPlanTypes();
                planType.setMeasurementPlan(plan);
                planType.setTypeOfMeasurement(type);
                activeTypes.add(planType);
            }
        }

        measurementPlanTypesRepository.saveAll(existingTypes); // archived
        measurementPlanTypesRepository.saveAll(activeTypes);   // reactivated + new

        return mapToMeasurementPlanResponse(plan, activeTypes, newTimes);
    }

    public MeasurementPlanResponse findMeasurementPlanByPersonalNumber(String personalNumber) {
        Optional<MeasurementPlan> optionalPlan = measurementPlanRepository.findByPersonalNumber(personalNumber);
        if (optionalPlan.isEmpty()) {
            return null;
        }
        MeasurementPlan plan = optionalPlan.get();

        List<MeasurementPlanTypes> activePlanTypes = measurementPlanTypesRepository.findAllActiveTypesByMeasurementPlanId(plan.getPlanId());
        List<MeasurementTime> measurementTimes = measurementTimeRepository.findAllByPlanPlanId(plan.getPlanId());

        return mapToMeasurementPlanResponse(plan, activePlanTypes, measurementTimes);
    }

//    private void validateMeasurementTimes(MeasurementPlanRequest request) {
//        int requiredTimes = request.getFrequency().getRequiredCountOfTimes();
//        if (request.getTimesOfPlannedMeasurements().size() != requiredTimes) {
//            throw new IllegalArgumentException(
//                    "Frequency " + request.getFrequency() + " requires: " + requiredTimes);
//        }
//    }

    private MeasurementPlan mapToMeasurementPlan(MeasurementPlanRequest request, Patient patient, Doctor doctor) {
        MeasurementPlan plan = new MeasurementPlan();
        plan.setPatient(patient);
        plan.setDoctor(doctor);
        plan.setFrequency(request.getFrequency());
        return plan;
    }


    private MeasurementPlanResponse mapToMeasurementPlanResponse(MeasurementPlan plan, List<MeasurementPlanTypes> planTypes, List<MeasurementTime> measurementTimes) {
        return new MeasurementPlanResponse(
                plan.getPlanId(),
                plan.getPatient().getPersonalNumber(),
                plan.getDoctor().getPanNumber(),
                plan.getFrequency().getDescription(),
                planTypes.stream().map(pt -> new MeasurementPlanTypesResponse(pt.getTypeOfMeasurement().getTypeId(), pt.getTypeOfMeasurement().getTypeName())).toList(),
                measurementTimes.stream().map(t -> t.getTime()).toList(),
                plan.getCreatedAt(),
                plan.getLastUpdateAt()
        );
    }
}
