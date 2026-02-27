package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.MeasurementRecordRequest;
import sk.uniza.fri.telemedicine.dto.response.MeasurementRecordResponse;
import sk.uniza.fri.telemedicine.entities.MeasurementRecord;
import sk.uniza.fri.telemedicine.entities.Patient;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;
import sk.uniza.fri.telemedicine.enums.others.StatusOfMeasurementRecord;
import sk.uniza.fri.telemedicine.helpers.EmailSender;
import sk.uniza.fri.telemedicine.repository.MeasurementRecordRepository;

import java.time.LocalDateTime;

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
        measurementRecordRepository.save(measurementRecord);

        StatusOfMeasurementRecord status = StatusOfMeasurementRecord.STATUS_OK;
        if (!checkIfRecordIsInRange(request.getValue(), typeOfMeasurement)) {
            status = StatusOfMeasurementRecord.STATUS_DOCTOR_INFORMED;
            emailSender.sendMeasurementRecordAlert(
                    patientService.getCareProviderEmailByPatientPersonalNumber(patient.getPersonalNumber()),
                    patientService.getPatientFullNameByPersonalNumber(patient.getPersonalNumber()),
                    request.getValue(), typeOfMeasurement.getUnits());
        }
        return this.mapToMeasurementRecordResponse(measurementRecord, status);
    }

    private boolean checkIfRecordIsInRange(Integer value, TypeOfMeasurement typeOfMeasurement) {
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

    private MeasurementRecordResponse mapToMeasurementRecordResponse(MeasurementRecord measurementRecord, StatusOfMeasurementRecord status) {
        return new MeasurementRecordResponse(measurementRecord.getTypeOfMeasurement().getTypeName(),
                measurementRecord.getValue(), measurementRecord.getTypeOfMeasurement().getUnits(),
                measurementRecord.getTimeOfMeasurement(), status);
    }
}
