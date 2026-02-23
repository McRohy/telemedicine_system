package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.DoctorRequest;
import sk.uniza.fri.telemedicine.dto.DoctorResponse;
import sk.uniza.fri.telemedicine.dto.PersonalDataResponse;
import sk.uniza.fri.telemedicine.entities.Doctor;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.enums.Specialization;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.ResourceNotFoundException;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

import java.util.ArrayList;
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
        Doctor doctor = new Doctor();
        doctor.setPanNumber(request.getPanNumber());
        doctor.setSpecialization(Specialization.valueOf(request.getSpecialization().toUpperCase()));
        doctor.setPersonalData(personalData);

        doctorRepository.save(doctor);
        return mapToDoctorResponse(doctor);
    }

    public List<DoctorResponse> getAllDoctors() {
        return doctorRepository.findAll()
                .stream()
                .map(doctor -> mapToDoctorResponse(doctor))
                .toList();
    }

    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
        return new DoctorResponse(new PersonalDataResponse(doctor.getPersonalData().getEmail(),
                doctor.getPersonalData().getFirstName(), doctor.getPersonalData().getLastName()),
                doctor.getSpecialization().toString());
    }
}
