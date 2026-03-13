package sk.uniza.fri.telemedicine.services.core;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.DoctorRequest;
import sk.uniza.fri.telemedicine.dto.response.DoctorResponse;
import sk.uniza.fri.telemedicine.entities.Doctor;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;
import java.util.List;

@Service
public class DoctorService {

    private final PersonalDataService personalDataService;
    private final DoctorRepository doctorRepository;

    public DoctorService(PersonalDataService personalDataService, DoctorRepository doctorRepository) {
        this.personalDataService = personalDataService;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        if (doctorRepository.existsByPanNumber(request.getPanNumber())) {
            throw new DuplicateException("Doctor with this PAN number already exists");
        }
        PersonalData personalData = personalDataService.createPersonalData(request.getPersonalData());
        Doctor doctor = mapToDoctor(request, personalData);
        doctorRepository.save(doctor);
        return mapToDoctorResponse(doctor);
    }

    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> mapToDoctorResponse(doctor))
                .toList();
    }

    public Doctor findByPanNumber(String panNumber) {
        return doctorRepository.findByPanNumber(panNumber).orElseThrow(
                () -> new NotFoundException("Doctor with PAN number not found"));
    }

    public DoctorResponse findDoctorByPanNumberResponse(String panNumber) {
        return mapToDoctorResponse(findByPanNumber(panNumber));
    }

    public String getFullNameByPanNumber(String panNumber) {
        return doctorRepository.findFullNameByPanNumber(panNumber).orElseThrow(
                () -> new NotFoundException("Doctor with PAN number not found"));
    }

    public String getPanNumberByEmail(String email) {
        return doctorRepository.findPanNumberByEmail(email).orElseThrow(
                () -> new NotFoundException("Doctor with email not found"));
    }

    private Doctor mapToDoctor(DoctorRequest request, PersonalData personalData) {
        Doctor doctor = new Doctor();
        doctor.setPanNumber(request.getPanNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setPersonalData(personalData);
        return doctor;
    }

    public DoctorResponse mapToDoctorResponse(Doctor doctor) {
        return new DoctorResponse(doctor.getPanNumber(),
                personalDataService.mapToPersonalDataResponse(doctor.getPersonalData()),
                doctor.getSpecialization());
    }
}
