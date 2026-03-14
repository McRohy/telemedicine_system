package sk.uniza.fri.telemedicine.services.core;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.MeasurementRecordRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementRecordResponse;
import sk.uniza.fri.telemedicine.entities.MeasurementRecord;
import sk.uniza.fri.telemedicine.entities.Patient;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;
import sk.uniza.fri.telemedicine.enums.constrains.MeasurementStatus;
import sk.uniza.fri.telemedicine.helpers.EmailSender;
import sk.uniza.fri.telemedicine.repository.MeasurementRecordRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MeasurementRecordService {

    private final PatientService patientService;
    private final MeasurementRecordRepository measurementRecordRepository;
    private final TypeOfMeasurementService typeOfMeasurementService;
    private final EmailSender emailSender;

    public MeasurementRecordService(PatientService patientService, MeasurementRecordRepository measurementRecordRepository,
                                  TypeOfMeasurementService typeOfMeasurementService, EmailSender emailSender) {
        this.patientService = patientService;
        this.measurementRecordRepository = measurementRecordRepository;
        this.typeOfMeasurementService = typeOfMeasurementService;
        this.emailSender = emailSender;
    }

    @Transactional
    public MeasurementRecordResponse trackNewMeasurement(MeasurementRecordRequest request) {
        Patient patient = patientService.findByPersonalNumber(request.getPersonalNumber());
        TypeOfMeasurement typeOfMeasurement = typeOfMeasurementService.findTypeOfMeasurementById(request.getTypeOfMeasurementId());

        MeasurementRecord measurementRecord = this.mapToMeasurementRecord(request, patient, typeOfMeasurement);

        if (!checkIfRecordIsInRange(request.getValue(), typeOfMeasurement)) {
            measurementRecord.setMeasurementStatus(MeasurementStatus.ABNORMAL);
            emailSender.sendMeasurementRecordAlert(
                    patientService.getCareProviderEmailByPatientPersonalNumber(patient.getPersonalNumber()),
                    patientService.getPatientFullNameByPersonalNumber(patient.getPersonalNumber()),
                    request.getValue(), typeOfMeasurement.getUnits());
        } else {
            measurementRecord.setMeasurementStatus(MeasurementStatus.NORMAL);
        }
        measurementRecordRepository.save(measurementRecord);
        return this.mapToMeasurementRecordResponse(measurementRecord);
    }

    private boolean checkIfRecordIsInRange(Double value, TypeOfMeasurement typeOfMeasurement) {
        return value >= typeOfMeasurement.getMinValue() && value <= typeOfMeasurement.getMaxValue();
    }

    public List<MeasurementRecordResponse> getMeasurementRecordForPatient(String personalNumber, Integer typeId, LocalDate period) {
        LocalDate from = period.withDayOfMonth(1);
        LocalDate to = period.withDayOfMonth(period.lengthOfMonth());
        return measurementRecordRepository
                .findAllByPatientAndTimeBetween(personalNumber,typeId, from, to).stream()
                .map(record -> this.mapToMeasurementRecordResponse(record))
                .toList();
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
        return new MeasurementRecordResponse(measurementRecord.getTypeOfMeasurement().getTypeName(),
                measurementRecord.getValue(), measurementRecord.getTypeOfMeasurement().getUnits(),
                measurementRecord.getTimeOfMeasurement(), measurementRecord.getMeasurementStatus().getDescription());
    }
}
