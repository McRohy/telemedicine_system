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
            mt.setValidFrom(LocalDateTime.now());
            measurementTimeRepository.save(mt);
        }

        List<MeasurementPlanTypes> activeTypes = new ArrayList<>();
        for (Integer typeId : request.getTypeOfMeasurementIds()) {
            TypeOfMeasurement type = typeOfMeasurementService.findTypeOfMeasurementById(typeId);
            MeasurementPlanTypes planType = new MeasurementPlanTypes();
            planType.setMeasurementPlan(plan);
            planType.setTypeOfMeasurement(type);
            planType.setValidFrom(LocalDateTime.now());
            measurementPlanTypesRepository.save(planType);
        }

        return mapToMeasurementPlanResponse(plan, activeTypes, measurementTimes);
    }


    @Transactional
    public MeasurementPlanResponse updateMeasurementPlan(Integer id, MeasurementPlanRequest request) {
        MeasurementPlan plan = measurementPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Measurement plan not found"));

        LocalDateTime now = LocalDateTime.now();
        plan.setFrequency(request.getFrequency());
        plan.setLastUpdateAt(now);
        measurementPlanRepository.save(plan);

        List<MeasurementTime> existingTimes = measurementTimeRepository.findAllActiveTimesByMeasurementPlanId(plan.getPlanId());
        List<LocalTime> requestedTimes = request.getTimesOfPlannedMeasurements();
        List<MeasurementTime> activeTimes = new ArrayList<>();

        for (MeasurementTime existing : existingTimes) {
            if (!requestedTimes.contains(existing.getTime())) {
                existing.setValidTo(now);
            } else {
                activeTimes.add(existing);
            }
        }
        measurementTimeRepository.saveAll(existingTimes);

        for (LocalTime time : requestedTimes) {
            boolean exists = false;
            for (MeasurementTime active : activeTimes) {
                if (active.getTime().equals(time)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                MeasurementTime mt = new MeasurementTime();
                mt.setPlan(plan);
                mt.setTime(time);
                mt.setValidFrom(now);
                activeTimes.add(mt);
            }
        }
        measurementTimeRepository.saveAll(activeTimes);

        List<MeasurementPlanTypes> existingTypes = measurementPlanTypesRepository.findAllActiveTypesByMeasurementPlanId(plan.getPlanId());
        List<Integer> requestedTypeIds = request.getTypeOfMeasurementIds();
        List<MeasurementPlanTypes> activeTypes = new ArrayList<>();

        for (MeasurementPlanTypes existing : existingTypes) {
            if (!requestedTypeIds.contains(existing.getTypeOfMeasurement().getTypeId())) {
                existing.setValidTo(now);
            } else {
                activeTypes.add(existing);
            }
        }
        measurementPlanTypesRepository.saveAll(existingTypes);

        for (Integer typeId : requestedTypeIds) {
            boolean exists = false;
            for (MeasurementPlanTypes active : activeTypes) {
                if (active.getTypeOfMeasurement().getTypeId() == typeId) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                TypeOfMeasurement type = typeOfMeasurementService.findTypeOfMeasurementById(typeId);
                MeasurementPlanTypes pt = new MeasurementPlanTypes();
                pt.setMeasurementPlan(plan);
                pt.setTypeOfMeasurement(type);
                pt.setValidFrom(now);
                activeTypes.add(pt);
            }
        }
        measurementPlanTypesRepository.saveAll(activeTypes);

        return mapToMeasurementPlanResponse(plan, activeTypes, activeTimes);
    }

    public Optional<MeasurementPlanResponse>  findMeasurementPlanByPersonalNumber(String personalNumber) {
        Optional<MeasurementPlan> optionalPlan = measurementPlanRepository.findByPersonalNumber(personalNumber);
        if (optionalPlan.isEmpty()) {
            return Optional.empty();
        }
        MeasurementPlan plan = optionalPlan.get();
        List<MeasurementPlanTypes> activePlanTypes = measurementPlanTypesRepository.findAllActiveTypesByMeasurementPlanId(plan.getPlanId());
        List<MeasurementTime> measurementTimes = measurementTimeRepository.findAllActiveTimesByMeasurementPlanId(plan.getPlanId());

        return Optional.of( mapToMeasurementPlanResponse(plan, activePlanTypes, measurementTimes));
    }

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
                plan.getFrequency(),
                planTypes.stream().map(pt -> new MeasurementPlanTypesResponse(pt.getTypeOfMeasurement().getTypeId(), pt.getTypeOfMeasurement().getTypeName(), pt.getTypeOfMeasurement().getUnits())).toList(),
                measurementTimes.stream().map(t -> t.getTime()).toList(),
                plan.getCreatedAt(),
                plan.getLastUpdateAt()
        );
    }
}
