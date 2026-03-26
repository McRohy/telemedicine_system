package sk.uniza.fri.telemedicine.services.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.PatientRequest;
import sk.uniza.fri.telemedicine.dto.response.PatientResponse;
import sk.uniza.fri.telemedicine.enums.Role;
import sk.uniza.fri.telemedicine.entities.Doctor;
import sk.uniza.fri.telemedicine.entities.Patient;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.PatientRepository;
import sk.uniza.fri.telemedicine.services.auth.AuthorizationService;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final PersonalDataService personalDataService;
    private final DoctorService doctorService;
    private final AuthorizationService authorizationService;

    public PatientService(PersonalDataService personalDataService, PatientRepository patientRepository,
                          DoctorService doctorService, AuthorizationService authorizationService) {
        this.personalDataService = personalDataService;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.authorizationService = authorizationService;
    }

    @Transactional
    public PatientResponse createPatient(PatientRequest request) {
        if (patientRepository.existsById(request.getPersonalNumber())) {
            throw new DuplicateException("Patient with this personal number already exists");
        }
        PersonalData personalData = personalDataService.createPersonalData(request.getPersonalData(), Role.PATIENT);
        Doctor doctor = doctorService.findByPanNumber(request.getPanNumber());
        Patient patient = mapToPatient(request, personalData, doctor);
        patientRepository.save(patient);
        return mapToPatientResponse(patient);
    }

    public Page<PatientResponse> getPatientsByDoctorPanNumber(String panNumber, int page, int size, String searchLastName) {
        authorizationService.authorizeDoctorIdentity(panNumber);
        Pageable pageable = PageRequest.of(page, size, Sort.by("personalData.lastName").ascending());
        if (searchLastName != null && !searchLastName.isBlank()) {
            return patientRepository.findByPanNumberAndPersonalDataLastNameStartingWithIgnoreCase(panNumber, searchLastName, pageable)
                    .map(patient -> mapToPatientResponse(patient));
        }
        return patientRepository.findAllByPanNumber(panNumber, pageable).map(p -> mapToPatientResponse(p));
    }

    public Page<PatientResponse> getPatients(int page, int size, String searchLastName) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("personalData.lastName").ascending());

        if (searchLastName != null && !searchLastName.isBlank()) {
            return patientRepository.findByPersonalDataLastNameStartingWithIgnoreCase(searchLastName, pageable)
                    .map(patient -> mapToPatientResponse(patient));
        }
        return patientRepository.findAll(pageable).map(p -> mapToPatientResponse(p));
    }

    public PatientResponse getPatientByPersonalNumber(String personalNumber) {
        authorizationService.authorizePatientDataAccess(personalNumber);
        return mapToPatientResponse(getByPersonalNumber(personalNumber));
    }

    public Patient getByPersonalNumber(String personalNumber) {
        return patientRepository.findById(personalNumber).orElseThrow(
                () -> new NotFoundException("Patient with personal number not found"));
    }

    public String getCareProviderEmailByPatientPersonalNumber(String personalNumber) {
        return patientRepository.findCareProviderEmailByPatientPersonalNumber(personalNumber).orElseThrow(
                () -> new NotFoundException("Patient with personal number not found")
        );
    }

    public String getPatientFullNameByPersonalNumber(String personalNumber) {
        return patientRepository.findFullNameByPersonalNumber(personalNumber)
                .orElseThrow(() -> new NotFoundException("Patient with personal number not found"));
    }

    public String getPatientPersonalNumberByEmail(String email) {
        return patientRepository.findPersonalNumberByEmail(email)
                .orElseThrow(() -> new NotFoundException("Patient with email not found"));
    }

    private Patient mapToPatient(PatientRequest request, PersonalData personalData, Doctor doctor) {
        Patient patient = new Patient();
        patient.setPersonalNumber(request.getPersonalNumber());
        patient.setPersonalData(personalData);
        patient.setDoctor(doctor);
        patient.setGender(request.getGender());
        return patient;
    }

    private PatientResponse mapToPatientResponse(Patient patient) {
        return new PatientResponse(
                patient.getPersonalNumber(),
                personalDataService.mapToPersonalDataResponse(patient.getPersonalData()),
                patient.getDoctor().getPanNumber(),
                patient.getGender()
        );
    }
}
