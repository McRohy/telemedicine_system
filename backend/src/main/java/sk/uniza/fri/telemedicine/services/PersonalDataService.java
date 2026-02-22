package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.PersonalDataRequest;
import sk.uniza.fri.telemedicine.dto.PersonalDataResponse;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.enums.Role;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

@Service
public class PersonalDataService {

    private final PersonalDataRepository personalDataRepository;

    public PersonalDataService(PersonalDataRepository personalDataRepository) {
        this.personalDataRepository = personalDataRepository;
    }

    @Transactional
    public PersonalData createPersonalData(PersonalDataRequest request) {
        PersonalData personalData = new PersonalData();
        personalData.setEmail(request.getEmail());
        personalData.setFirstName(request.getFirstName());
        personalData.setLastName(request.getLastName());
        personalData.setPassword("123");
        personalData.setRole(Role.valueOf(request.getRole().toUpperCase()));
        return personalDataRepository.save(personalData);
    }

    public PersonalDataResponse getPersonalDataByEmail(String email) {
        PersonalData personalData = personalDataRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Personal data not found"));
        return new PersonalDataResponse(personalData.getEmail(), personalData.getFirstName(), personalData.getLastName());
    }
}
