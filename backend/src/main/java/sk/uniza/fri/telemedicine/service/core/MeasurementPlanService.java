package sk.uniza.fri.telemedicine.service.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.config.TextProvider;
import sk.uniza.fri.telemedicine.dto.request.MeasurementPlanRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementPlanResponse;
import sk.uniza.fri.telemedicine.entity.*;
import sk.uniza.fri.telemedicine.exception.BusinessRuleException;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.MeasurementPlanRepository;
import sk.uniza.fri.telemedicine.repository.MeasurementTypePlanRepository;
import sk.uniza.fri.telemedicine.repository.MeasurementTimePlanRepository;
import sk.uniza.fri.telemedicine.service.auth.AuthorizationService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing measurement plans for patients.
 */
@Service
public class MeasurementPlanService {

    private final MeasurementPlanRepository measurementPlanRepository;
    private final MeasurementTypePlanRepository measurementTypePlanRepository;
    private final MeasurementTimePlanRepository measurementTimePlanRepository;

    private final TypeOfMeasurementService typeOfMeasurementService;
    private final PatientService patientService;
    private final EmailService emailService;
    private final AuthorizationService authorizationService;
    private final TextProvider textProvider;

    public MeasurementPlanService(MeasurementPlanRepository measurementPlanRepository, TypeOfMeasurementService typeOfMeasurementService,
                                  MeasurementTypePlanRepository measurementTypePlanRepository, MeasurementTimePlanRepository measurementTimePlanRepository,
                                  PatientService patientService, EmailService emailService, AuthorizationService authorizationService,
                                  TextProvider textProvider) {
        this.measurementPlanRepository = measurementPlanRepository;
        this.typeOfMeasurementService = typeOfMeasurementService;
        this.measurementTypePlanRepository = measurementTypePlanRepository;
        this.measurementTimePlanRepository = measurementTimePlanRepository;
        this.patientService = patientService;
        this.emailService = emailService;
        this.authorizationService = authorizationService;
        this.textProvider = textProvider;
    }

    /**
     * Returns the active measurement plan for specific patient.
     */
    @Transactional(readOnly = true)  //reads in one transaction to keep data consistent
    public MeasurementPlanResponse getMeasurementPlanByPersonalNumber(String personalNumber) {
        authorizationService.authorizePatientDataAccess(personalNumber);
        MeasurementPlan plan = measurementPlanRepository.findActivePlanByPersonalNumber(personalNumber)
                .orElseThrow(() -> new NotFoundException(textProvider.get("error.measurementPlan.notFound")));

        List<MeasurementTypePlan> measurementTypes = measurementTypePlanRepository.findAllByPlanId(plan.getPlanId());
        List<MeasurementTimePlan> measurementTimes = measurementTimePlanRepository.findAllByPlanId(plan.getPlanId());

        return mapToMeasurementPlanResponse(plan, measurementTypes, measurementTimes);
    }

    /**
     * Creates a new measurement plan for patient.
     *  The patient can have only one active plan at a time.
     */
    @Transactional
    public MeasurementPlanResponse createMeasurementPlan(MeasurementPlanRequest request) {
        authorizationService.authorizePatientDataAccess(request.getPersonalNumber());
        validateFrequencyAndTimes(request);
        Patient patient = patientService.getByPersonalNumber(request.getPersonalNumber());

        if (measurementPlanRepository.existsActivePlanByPersonalNumber(request.getPersonalNumber())) {
            throw new DuplicateException(textProvider.get("error.measurementPlan.duplicate"));
        }

        MeasurementPlan plan = createPlan(request, patient);
        List<MeasurementTimePlan> measurementTimes = createTimeForPlan(plan, request);
        List<MeasurementTypePlan> measurementTypes = createTypesForPlan(plan, request);
        emailService.sendEmailCreatedPlan(patient.getPersonalData().getEmail());

        return mapToMeasurementPlanResponse(plan, measurementTypes, measurementTimes);
    }

    /**
     * Updates a measurement plan by deactivating the current plan and creates a new version of plan.
     */
    @Transactional
    public MeasurementPlanResponse updateMeasurementPlan(Long id, MeasurementPlanRequest request) {
        authorizationService.authorizePatientDataAccess(request.getPersonalNumber());
        MeasurementPlan plan = measurementPlanRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(textProvider.get("error.measurementPlan.notFound")));

        if (plan.getValidTo() != null) {
            throw new BusinessRuleException(textProvider.get("error.measurementPlan.alreadyDeactivated"));
        }

        if (!plan.getPatient().getPersonalNumber().equals(request.getPersonalNumber())) {
            throw new BusinessRuleException(textProvider.get("error.measurementPlan.wrongPatient"));
        }

        validateFrequencyAndTimes(request);
        Patient patient = patientService.getByPersonalNumber(request.getPersonalNumber());

        LocalDateTime now = LocalDateTime.now();
        plan.setValidTo(now);
        measurementPlanRepository.save(plan);

        MeasurementPlan newPlan = createPlan(request, patient);
        List<MeasurementTimePlan> newMeasurementTimes = createTimeForPlan(newPlan, request);
        List<MeasurementTypePlan> newMeasurementTypes = createTypesForPlan(newPlan, request);
        emailService.sendEmailUpdatedPlan(patient.getPersonalData().getEmail());

        return mapToMeasurementPlanResponse(newPlan, newMeasurementTypes, newMeasurementTimes);
    }

    public void validateActivePlanAndType(String personalNumber, Long typeId) {
        if (!measurementPlanRepository.existsActivePlanByPersonalNumber(personalNumber)) {
            throw new NotFoundException(textProvider.get("error.measurementPlan.notActive"));
        }
        if (!measurementTypePlanRepository.existsByActivePlanAndTypeId(personalNumber, typeId)) {
            throw new BusinessRuleException(textProvider.get("error.measurementPlan.typeNotInPlan"));
        }
    }

    private void validateFrequencyAndTimes(MeasurementPlanRequest request) {
        int expected = request.getFrequency().getExpectedTimes();
        int actual = request.getTimesOfPlannedMeasurements().size();
        if (actual != expected) {
            throw new BusinessRuleException(textProvider.get("error.measurementPlan.frequencyTimesMismatch"));
        }
    }

    private MeasurementPlan createPlan(MeasurementPlanRequest request, Patient patient) {
        MeasurementPlan plan = new MeasurementPlan();
        plan.setPatient(patient);
        plan.setFrequency(request.getFrequency());
        plan.setValidFrom(LocalDateTime.now());
        measurementPlanRepository.save(plan);
        return plan;
    }

    private List<MeasurementTimePlan> createTimeForPlan(MeasurementPlan plan, MeasurementPlanRequest request) {
        List<MeasurementTimePlan> newTimes = new ArrayList<>();
        for (LocalTime time : request.getTimesOfPlannedMeasurements()) {
            MeasurementTimePlan timePlan = new MeasurementTimePlan();
            timePlan.setMeasurementPlan(plan);
            timePlan.setTime(time);
            newTimes.add(timePlan);
        }
        measurementTimePlanRepository.saveAll(newTimes);
        return newTimes;
    }

    private List<MeasurementTypePlan> createTypesForPlan(MeasurementPlan plan, MeasurementPlanRequest request) {
        List<MeasurementTypePlan> newTypes = new ArrayList<>();
        for (Long typeId : request.getTypeOfMeasurementIds()) {
            TypeOfMeasurement type = typeOfMeasurementService.getTypeOfMeasurementById(typeId);
            MeasurementTypePlan typePlan = new MeasurementTypePlan();
            typePlan.setMeasurementPlan(plan);
            typePlan.setTypeOfMeasurement(type);
            newTypes.add(typePlan);
        }
        measurementTypePlanRepository.saveAll(newTypes);
        return newTypes;
    }

    private MeasurementPlanResponse mapToMeasurementPlanResponse(MeasurementPlan plan, List<MeasurementTypePlan> planTypes, List<MeasurementTimePlan> measurementTimePlans) {
        return new MeasurementPlanResponse(
                plan.getPlanId(),
                plan.getPatient().getPersonalNumber(),
                plan.getFrequency(),
                planTypes.stream().map(pt -> typeOfMeasurementService.mapToTypeOfMeasurementShortResponse(pt.getTypeOfMeasurement())).toList(),
                measurementTimePlans.stream().map(t -> t.getTime()).toList(),
                plan.getValidFrom()
        );
    }
}
