package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.*;
import sk.uniza.fri.telemedicine.entities.Doctor;
import sk.uniza.fri.telemedicine.entities.Patient;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.enums.Specialization;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;
import sk.uniza.fri.telemedicine.repository.PatientRepository;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private final PersonalDataService personalDataService;
    private final PersonalDataRepository personalDataRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public PatientService(PersonalDataService personalDataService, PersonalDataRepository personalDataRepository, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.personalDataService = personalDataService;
        this.personalDataRepository = personalDataRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.existsById(request.getPersonalNumber())){
            throw new RuntimeException("Patient with this personal number already exists");
        }

        PersonalData personalData = personalDataService.createPersonalData(request.getPersonalData());
        personalDataRepository.findById(personalData.getEmail()).orElseThrow(() -> new RuntimeException("Personal data not found"));

        Doctor doctor = doctorRepository.findById(request.getPanNumber()).orElseThrow(() -> new RuntimeException("Doctor not found"));

        Patient patient = new Patient();
        patient.setPersonalNumber(request.getPersonalNumber());
        patient.setPersonalData(personalData);
        patient.setDoctor(doctor);
        patientRepository.save(patient);

        return new PatientResponse(new PersonalDataResponse(personalData.getEmail(), personalData.getFirstName(), personalData.getLastName()), patient.getPersonalNumber());
    }

    public List<PatientResponse> getAllByDoctorsPanNumber(Integer panNumber) {
        List<Patient> patients = patientRepository.findAllByPanNumber(panNumber);
        List<PatientResponse> patientResponse = new ArrayList<>();
        for (Patient p : patients) {
            patientResponse.add(new PatientResponse(new PersonalDataResponse(p.getPersonalData().getEmail(), p.getPersonalData().getFirstName(), p.getPersonalData().getLastName()), p.getPersonalNumber()));
        }
        return patientResponse;
    }
}
