package sk.uniza.fri.telemedicine.services.core;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.MeasurementRecordRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementRecordResponse;
import sk.uniza.fri.telemedicine.entities.MeasurementRecord;
import sk.uniza.fri.telemedicine.entities.Patient;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;
import sk.uniza.fri.telemedicine.enums.MeasurementStatus;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.MeasurementPlanRepository;
import sk.uniza.fri.telemedicine.repository.MeasurementRecordRepository;
import sk.uniza.fri.telemedicine.repository.MeasurementTypePlanRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementRecordService {

    private final PatientService patientService;
    private final MeasurementRecordRepository measurementRecordRepository;
    private final MeasurementPlanRepository measurementPlanRepository;
    private final MeasurementTypePlanRepository measurementTypePlanRepository;
    private final TypeOfMeasurementService typeOfMeasurementService;
    private final EmailService emailService;

    public MeasurementRecordService(PatientService patientService, MeasurementRecordRepository measurementRecordRepository,
                                    MeasurementPlanRepository measurementPlanRepository, MeasurementTypePlanRepository measurementTypePlanRepository,
                                    TypeOfMeasurementService typeOfMeasurementService, EmailService emailService) {
        this.patientService = patientService;
        this.measurementRecordRepository = measurementRecordRepository;
        this.measurementPlanRepository = measurementPlanRepository;
        this.measurementTypePlanRepository = measurementTypePlanRepository;
        this.typeOfMeasurementService = typeOfMeasurementService;
        this.emailService = emailService;
    }

    @Transactional
    public MeasurementRecordResponse createMeasurementRecord(MeasurementRecordRequest request) {
        if (!measurementPlanRepository.existsActivePlanByPersonalNumber(request.getPersonalNumber())) {
            throw new NotFoundException("Patient does not have an active measurement plan");
        }
        if (!measurementTypePlanRepository.existsByActivePlanAndTypeId(request.getPersonalNumber(), request.getTypeOfMeasurementId())) {
            throw new IllegalArgumentException("Measurement type is not part of the patient's active plan");
        }
        Patient patient = patientService.findByPersonalNumber(request.getPersonalNumber());
        TypeOfMeasurement typeOfMeasurement = typeOfMeasurementService.findTypeOfMeasurementById(request.getTypeOfMeasurementId());
        MeasurementRecord measurementRecord = mapToMeasurementRecord(request, patient, typeOfMeasurement);

        if (!checkIfRecordIsInRange(request.getValue(), typeOfMeasurement)) {
            measurementRecord.setMeasurementStatus(MeasurementStatus.ABNORMAL);
            emailService.sendMeasurementRecordAlert(
                    patientService.getCareProviderEmailByPatientPersonalNumber(patient.getPersonalNumber()),
                    patientService.getPatientFullNameByPersonalNumber(patient.getPersonalNumber()),
                    request.getValue(), typeOfMeasurement.getUnits());
        } else {
            measurementRecord.setMeasurementStatus(MeasurementStatus.NORMAL);
        }
        measurementRecordRepository.save(measurementRecord);
        return mapToMeasurementRecordResponse(measurementRecord);
    }

    public List<MeasurementRecordResponse> getMeasurementRecords(String personalNumber, Long typeId, LocalDate period) {
        LocalDate from = period.withDayOfMonth(1);
        LocalDate to = period.withDayOfMonth(period.lengthOfMonth());
        return measurementRecordRepository
                .findAllByPatientAndTimeBetween(personalNumber, typeId, from, to).stream()
                .map(record -> mapToMeasurementRecordResponse(record))
                .toList();
    }

    public Page<MeasurementRecordResponse> getPagedMeasurementRecords(String personalNumber, int page, int size, Long typeId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("typeOfMeasurement.typeName").ascending());
        if (typeId != null) {
            return measurementRecordRepository.findByPersonalNumberAndMeasurementTypeId(personalNumber, typeId, pageable)
                    .map(patient -> mapToMeasurementRecordResponse(patient));
        }
        return measurementRecordRepository.findByPersonalNumber(personalNumber, pageable).map(p -> mapToMeasurementRecordResponse(p));
    }

    private boolean checkIfRecordIsInRange(Double value, TypeOfMeasurement typeOfMeasurement) {
        return value >= typeOfMeasurement.getMinValue() && value <= typeOfMeasurement.getMaxValue();
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
                measurementRecord.getTypeOfMeasurement().getTypeName(),
                measurementRecord.getValue(),
                measurementRecord.getTypeOfMeasurement().getUnits(),
                measurementRecord.getTimeOfMeasurement(),
                measurementRecord.getMeasurementStatus(), measurementRecord.getNote()
        );
    }
}
