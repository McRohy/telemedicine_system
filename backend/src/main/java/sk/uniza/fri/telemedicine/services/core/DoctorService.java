package sk.uniza.fri.telemedicine.services.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.DoctorRequest;
import sk.uniza.fri.telemedicine.dto.response.DoctorResponse;
import sk.uniza.fri.telemedicine.entities.Doctor;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;

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

    public Page<DoctorResponse> getDoctors(int page, int size, String searchLastName) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("personalData.lastName").ascending());

        if (searchLastName != null && !searchLastName.isBlank()) {
            return doctorRepository.findByPersonalDataLastNameStartingWithIgnoreCase(searchLastName, pageable)
                    .map(doctor -> mapToDoctorResponse(doctor));
        }
        return doctorRepository.findAll(pageable)
                .map(doctor -> mapToDoctorResponse(doctor));

    }

    public Doctor findByPanNumber(String panNumber) {
        return doctorRepository.findById(panNumber).orElseThrow(
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

    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
        return new DoctorResponse(
                doctor.getPanNumber(),
                personalDataService.mapToPersonalDataResponse(doctor.getPersonalData()),
                doctor.getSpecialization()
        );
    }
}
