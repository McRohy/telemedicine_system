package sk.uniza.fri.telemedicine.services.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.PasswordRequest;
import sk.uniza.fri.telemedicine.dto.request.PersonalDataRequest;
import sk.uniza.fri.telemedicine.dto.response.PersonalDataResponse;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import org.springframework.security.crypto.password.PasswordEncoder;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

import java.util.UUID;

@Service
public class PersonalDataService {

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    private final PersonalDataRepository personalDataRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PersonalDataService(PersonalDataRepository personalDataRepository, EmailService emailService,
                               PasswordEncoder passwordEncoder) {
        this.personalDataRepository = personalDataRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public PersonalData createPersonalData(PersonalDataRequest request) {
        if (personalDataRepository.existsById(request.getEmail())){
            throw new DuplicateException("Personal data with this email already exists");
        }
        PersonalData personalData = mapToPersonalData(request);
        setUpPassword(personalData, request.getEmail());
        return personalDataRepository.save(personalData);
    }

    private void setUpPassword(PersonalData personalData, String email) {
        personalData.setPassword(null);
        String token = UUID.randomUUID().toString();
        personalData.setSetupToken(token);
        String link = frontendBaseUrl + "/password/" + token;
        emailService.sendEmailWithTokenPassword(email, link);
    }

    @Transactional
    public void setPassword(PasswordRequest request) {
       PersonalData personalData = personalDataRepository.findBySetupToken(request.getToken())
                .orElseThrow(() -> new NotFoundException("Token not found"));

       String password = passwordEncoder.encode(request.getPassword());
       personalData.setPassword(password);
       personalData.setSetupToken(null);
       personalDataRepository.save(personalData);
       emailService.sendEmailSuccessfulPasswordSetUp(personalData.getEmail());
    }

    public PersonalData getByEmail(String email) {
        return personalDataRepository.findById(email)
                .orElseThrow(() -> new NotFoundException("Personal data with this email not found"));
    }

    private PersonalData mapToPersonalData(PersonalDataRequest request) {
        PersonalData personalData = new PersonalData();
        personalData.setEmail(request.getEmail());
        personalData.setFirstName(request.getFirstName());
        personalData.setLastName(request.getLastName());
        personalData.setRole(request.getRole());
        return personalData;
    }

    public PersonalDataResponse mapToPersonalDataResponse(PersonalData personalData) {
        return new PersonalDataResponse(
                personalData.getEmail(),
                personalData.getFirstName(),
                personalData.getLastName()
        );
    }
}
