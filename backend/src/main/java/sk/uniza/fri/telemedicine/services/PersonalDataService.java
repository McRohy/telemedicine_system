package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sk.uniza.fri.telemedicine.dto.request.PasswordRequest;
import sk.uniza.fri.telemedicine.dto.request.PersonalDataRequest;
import sk.uniza.fri.telemedicine.dto.response.PersonalDataResponse;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.enums.constrains.Role;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.ResourceNotFoundException;
import sk.uniza.fri.telemedicine.helpers.EmailSender;
import sk.uniza.fri.telemedicine.helpers.SecurityConfiger;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

import java.util.Random;
import java.util.UUID;

@Service
public class PersonalDataService {

    private final PersonalDataRepository personalDataRepository;
    private final EmailSender emailSender;
    private final SecurityConfiger securityConfiger;

    public PersonalDataService(PersonalDataRepository personalDataRepository, EmailSender emailSender, SecurityConfiger securityConfiger) {
        this.personalDataRepository = personalDataRepository;
        this.emailSender = emailSender;
        this.securityConfiger = securityConfiger;
    }

    @Transactional
    public PersonalData createPersonalData(PersonalDataRequest request) {
        if (personalDataRepository.existsById(request.getEmail())){
            throw new DuplicateException("Personal data with this email already exists");
        }
        PersonalData personalData = this.mapToPersonalData(request);
        this.setUpPassword(personalData, request.getEmail());
        personalDataRepository.save(personalData);
        return personalDataRepository.save(personalData);
    }

    private void setUpPassword(PersonalData personalData, String email) {
        personalData.setPassword(null);
        String token = UUID.randomUUID().toString();
        personalData.setSetupToken(token);
        String link = "http://localhost:5173/password/" + token;
        emailSender.sendEmailWithPassword(email, link);
    }

    @Transactional
    public void setPassword(PasswordRequest request) {
       PersonalData personalData = personalDataRepository.findBySetupToken(request.getToken())
                .orElseThrow(() -> new ResourceNotFoundException("Token not found"));

       String password = securityConfiger.passwordEncoder().encode(request.getPassword());
       personalData.setPassword(password);
       personalData.setSetupToken(null);
       personalDataRepository.save(personalData);
    }

    private PersonalData mapToPersonalData(PersonalDataRequest request) {
        PersonalData personalData = new PersonalData();
        personalData.setEmail(request.getEmail());
        personalData.setFirstName(request.getFirstName());
        personalData.setLastName(request.getLastName());
        personalData.setRole(Role.valueOf(request.getRole().toUpperCase()));
        return personalData;
    }

    public PersonalDataResponse mapToPersonalDataResponse(PersonalData personalData) {
        return new PersonalDataResponse(personalData.getEmail(), personalData.getFirstName(),
                personalData.getLastName());
    }
}
