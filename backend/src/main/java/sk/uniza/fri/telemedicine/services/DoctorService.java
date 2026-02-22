package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.DoctorRequest;
import sk.uniza.fri.telemedicine.dto.DoctorResponse;
import sk.uniza.fri.telemedicine.dto.PersonalDataResponse;
import sk.uniza.fri.telemedicine.entities.Doctor;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.enums.Specialization;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {

    private final PersonalDataService personalDataService;
    private final PersonalDataRepository personalDataRepository;
    private final DoctorRepository doctorRepository;

    public DoctorService(PersonalDataService personalDataService, PersonalDataRepository personalDataRepository, DoctorRepository doctorRepository) {
        this.personalDataService = personalDataService;
        this.personalDataRepository = personalDataRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public Doctor createDoctor(DoctorRequest request) {
        PersonalData personalData = personalDataService.createPersonalData(request.getPersonalData());
        personalDataRepository.findById(personalData.getEmail()).orElseThrow(() -> new RuntimeException("Personal data not found"));

        Doctor doctor = new Doctor();
        doctor.setPanNumber(request.getPanNumber());
        doctor.setSpecialization(Specialization.valueOf(request.getSpecialization().toUpperCase()));
        doctor.setPersonalData(personalData);

        return doctorRepository.save(doctor);
    }

    public List<DoctorResponse> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        List<DoctorResponse> doctorResponses = new ArrayList<>();

        for (Doctor d : doctors) {
            PersonalDataResponse personalData = personalDataService.getPersonalDataByEmail(d.getPersonalData().getEmail());
            DoctorResponse response = new DoctorResponse(personalData, d.getSpecialization().toString());
            doctorResponses.add(response);
        }

        return doctorResponses;
    }
}
