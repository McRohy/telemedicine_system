package sk.uniza.fri.telemedicine.service.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.MeasurementRecordRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementRecordResponse;
import sk.uniza.fri.telemedicine.entity.MeasurementRecord;
import sk.uniza.fri.telemedicine.entity.Patient;
import sk.uniza.fri.telemedicine.entity.TypeOfMeasurement;
import sk.uniza.fri.telemedicine.enumeration.MeasurementStatus;
import sk.uniza.fri.telemedicine.repository.MeasurementRecordRepository;
import sk.uniza.fri.telemedicine.service.auth.AuthorizationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing measurement records.
 */
@Service
public class MeasurementRecordService {

    private final PatientService patientService;
    private final MeasurementRecordRepository measurementRecordRepository;
    private final MeasurementPlanService measurementPlanService;
    private final TypeOfMeasurementService typeOfMeasurementService;
    private final EmailService emailService;
    private final AuthorizationService authorizationService;

    public MeasurementRecordService(PatientService patientService, MeasurementRecordRepository measurementRecordRepository,
                                    MeasurementPlanService measurementPlanService,
                                    TypeOfMeasurementService typeOfMeasurementService, EmailService emailService,
                                    AuthorizationService authorizationService) {
        this.patientService = patientService;
        this.measurementRecordRepository = measurementRecordRepository;
        this.measurementPlanService = measurementPlanService;
        this.typeOfMeasurementService = typeOfMeasurementService;
        this.emailService = emailService;
        this.authorizationService = authorizationService;
    }

    /**
     * Creates new measurement record and sends email alert if the value is out of range defined in type of measurement.
     */
    @Transactional
    public MeasurementRecordResponse createMeasurementRecord(MeasurementRecordRequest request) {
        authorizationService.authorizePatientDataAccess(request.getPersonalNumber());
        measurementPlanService.validateActivePlanAndType(request.getPersonalNumber(), request.getTypeOfMeasurementId());

        Patient patient = patientService.getByPersonalNumber(request.getPersonalNumber());
        TypeOfMeasurement typeOfMeasurement = typeOfMeasurementService.getTypeOfMeasurementById(request.getTypeOfMeasurementId());
        MeasurementRecord measurementRecord = mapToMeasurementRecord(request, patient, typeOfMeasurement);

        if (!typeOfMeasurementService.isValueInNormRange(typeOfMeasurement, request.getValue())) {
            measurementRecord.setMeasurementStatus(MeasurementStatus.ABNORMAL);
        } else {
            measurementRecord.setMeasurementStatus(MeasurementStatus.NORMAL);
        }
        measurementRecordRepository.save(measurementRecord);

        if (measurementRecord.getMeasurementStatus() == MeasurementStatus.ABNORMAL) {
            emailService.sendMeasurementRecordAlert(
                    patientService.getCareProviderEmailByPatientPersonalNumber(patient.getPersonalNumber()),
                    patientService.getPatientFullNameByPersonalNumber(patient.getPersonalNumber()),
                    request.getValue(), typeOfMeasurement.getUnits());
        }
        return mapToMeasurementRecordResponse(measurementRecord);
    }

    /**
     * Returns a list of measurement records for specific patient, type of measurement and month of measurement.
     */
    public List<MeasurementRecordResponse> getMeasurementRecords(String personalNumber, Long typeId, LocalDate period) {
        authorizationService.authorizePatientDataAccess(personalNumber);
        LocalDate from = period.withDayOfMonth(1);
        LocalDate to = period.withDayOfMonth(period.lengthOfMonth());
        return measurementRecordRepository
                .findAllByPatientAndTimeBetween(personalNumber, typeId, from, to).stream()
                .map(record -> mapToMeasurementRecordResponse(record))
                .toList();
    }

    /**
     * Returns a paginated list of measurement records for specific patient with optional type of measurement filter.
     */
    public Page<MeasurementRecordResponse> getPagedMeasurementRecords(String personalNumber, int page, int size, Long typeId) {
        authorizationService.authorizePatientDataAccess(personalNumber);
        Pageable pageable = PageRequest.of(page, size, Sort.by("timeOfMeasurement").descending());
        if (typeId != null) {
            return measurementRecordRepository.findByPersonalNumberAndMeasurementTypeId(personalNumber, typeId, pageable)
                    .map(record -> mapToMeasurementRecordResponse(record));
        }
        return measurementRecordRepository.findByPersonalNumber(personalNumber, pageable).map(record -> mapToMeasurementRecordResponse(record));
    }

    private MeasurementRecord mapToMeasurementRecord(MeasurementRecordRequest request, Patient patient, TypeOfMeasurement typeOfMeasurement) {
        MeasurementRecord measurementRecord = new MeasurementRecord();
        measurementRecord.setTimeOfMeasurement(LocalDateTime.now());
        measurementRecord.setPatient(patient);
        measurementRecord.setTypeOfMeasurement(typeOfMeasurement);
        measurementRecord.setValue(request.getValue());
        measurementRecord.setNote(request.getNote());
        return measurementRecord;
    }

    private MeasurementRecordResponse mapToMeasurementRecordResponse(MeasurementRecord measurementRecord) {
        return new MeasurementRecordResponse(
                measurementRecord.getRecordId(),
                measurementRecord.getValue(),
                typeOfMeasurementService.mapToTypeOfMeasurementShortResponse(measurementRecord.getTypeOfMeasurement()),
                measurementRecord.getTimeOfMeasurement(),
                measurementRecord.getMeasurementStatus(),
                measurementRecord.getNote()
        );
    }
}
