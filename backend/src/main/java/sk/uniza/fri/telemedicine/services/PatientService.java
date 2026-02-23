package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.*;
import sk.uniza.fri.telemedicine.entities.Doctor;
import sk.uniza.fri.telemedicine.entities.Patient;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.ResourceNotFoundException;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;
import sk.uniza.fri.telemedicine.repository.PatientRepository;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private final PersonalDataService personalDataService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PatientService(PersonalDataService personalDataService, PatientRepository patientRepository,
                          DoctorRepository doctorRepository) {
        this.personalDataService = personalDataService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.existsById(request.getPersonalNumber())) {
            throw new DuplicateException("Patient with this personal number already exists");
        }

        PersonalData personalData = personalDataService.createPersonalData(request.getPersonalData());
        Doctor doctor = doctorRepository.findById(request.getPanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Doctor with pan number: " + request.getPanNumber() + " not exists"));

        Patient patient = new Patient();
        patient.setPersonalNumber(request.getPersonalNumber());
        patient.setPersonalData(personalData);
        patient.setDoctor(doctor);

        patientRepository.save(patient);
        return mapToPatientResponse(patient);
    }

    public List<PatientResponse> getAllByDoctorsPanNumber(Integer panNumber) {
        return patientRepository.findAllByPanNumber(panNumber)
                .stream()
                .map(p -> mapToPatientResponse(p))
                .toList();
    }

    private PatientResponse mapToPatientResponse(Patient patient) {
        return new PatientResponse(new PersonalDataResponse(patient.getPersonalData().getEmail(),
                patient.getPersonalData().getFirstName(), patient.getPersonalData().getLastName()), patient.getPersonalNumber());
    }
}
