package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.DoctorRequest;
import sk.uniza.fri.telemedicine.dto.response.DoctorResponse;
import sk.uniza.fri.telemedicine.entities.Doctor;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.enums.constrains.Specialization;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.ResourceNotFoundException;
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
        if (doctorRepository.existsById(request.getPanNumber())) {
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

    public Doctor findByPanNumber(Integer panNumber) {
        return doctorRepository.findById(panNumber).orElseThrow(
                () -> new ResourceNotFoundException("Doctor with PAN number not found"));
    }

    public DoctorResponse findDoctorByPanNumberResponse(Integer panNumber) {
        return mapToDoctorResponse(findByPanNumber(panNumber));
    }

    public String getFullNameByPanNumber(Integer panNumber) {
        return doctorRepository.findFullNameByPanNumber(panNumber).orElseThrow(
                () -> new ResourceNotFoundException("Doctor with PAN number not found"));
    }

    private Doctor mapToDoctor(DoctorRequest request, PersonalData personalData) {
        Doctor doctor = new Doctor();
        doctor.setPanNumber(request.getPanNumber());
        doctor.setSpecialization(Specialization.valueOf(request.getSpecialization().toUpperCase()));
        doctor.setPersonalData(personalData);
        return doctor;
    }

    public DoctorResponse mapToDoctorResponse(Doctor doctor) {
        return new DoctorResponse(doctor.getPanNumber(),
                personalDataService.mapToPersonalDataResponse(doctor.getPersonalData()),
                doctor.getSpecialization());
    }
}
