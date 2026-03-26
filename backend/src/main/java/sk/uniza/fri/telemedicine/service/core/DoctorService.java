package sk.uniza.fri.telemedicine.service.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.DoctorRequest;
import sk.uniza.fri.telemedicine.dto.response.DoctorResponse;
import sk.uniza.fri.telemedicine.enumeration.Role;
import sk.uniza.fri.telemedicine.entity.Doctor;
import sk.uniza.fri.telemedicine.entity.PersonalData;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;

/**
 * Service for managing doctors.
 */
@Service
public class DoctorService {

    private final PersonalDataService personalDataService;
    private final DoctorRepository doctorRepository;

    public DoctorService(PersonalDataService personalDataService, DoctorRepository doctorRepository) {
        this.personalDataService = personalDataService;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Creates new doctor and sends email for password setup.
     * The PAN number must be unique.
     */
    @Transactional
    public DoctorResponse createDoctor(DoctorRequest request) {
        if (doctorRepository.existsById(request.getPanNumber())) {
            throw new DuplicateException("Doctor with this PAN number already exists");
        }
        PersonalData personalData = personalDataService.createPersonalData(request.getPersonalData(), Role.DOCTOR);
        Doctor doctor = mapToDoctor(request, personalData);
        doctorRepository.save(doctor);
        return mapToDoctorResponse(doctor);
    }

    /**
     * Returns a paginated list of doctors with optional last name search.
     */
    public Page<DoctorResponse> getDoctors(int page, int size, String searchLastName) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("personalData.lastName").ascending());

        if (searchLastName != null && !searchLastName.isBlank()) {
            return doctorRepository.findByPersonalDataLastNameStartingWithIgnoreCase(searchLastName, pageable)
                    .map(doctor -> mapToDoctorResponse(doctor));
        }
        return doctorRepository.findAll(pageable)
                .map(doctor -> mapToDoctorResponse(doctor));

    }

    public Doctor getByPanNumber(String panNumber) {
        return doctorRepository.findById(panNumber).orElseThrow(
                () -> new NotFoundException("Doctor with PAN number not found"));
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
