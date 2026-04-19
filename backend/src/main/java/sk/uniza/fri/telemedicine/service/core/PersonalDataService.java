package sk.uniza.fri.telemedicine.service.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.config.TextProvider;
import sk.uniza.fri.telemedicine.dto.request.PasswordRequest;
import sk.uniza.fri.telemedicine.dto.request.PersonalDataRequest;
import sk.uniza.fri.telemedicine.dto.response.PersonalDataResponse;
import sk.uniza.fri.telemedicine.entity.PersonalData;
import org.springframework.security.crypto.password.PasswordEncoder;
import sk.uniza.fri.telemedicine.enumeration.Role;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

import java.util.UUID;

/**
 * Service for managing personal data of users.
 */
@Service
public class PersonalDataService {

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    private final PersonalDataRepository personalDataRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TextProvider textProvider;

    public PersonalDataService(PersonalDataRepository personalDataRepository, EmailService emailService,
                               PasswordEncoder passwordEncoder, TextProvider textProvider) {
        this.personalDataRepository = personalDataRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.textProvider = textProvider;
    }

    /**
     * Creates personal data of new user and sends email for password setup.
     */
    @Transactional
    public PersonalData createPersonalData(PersonalDataRequest request, Role role) {
        if (personalDataRepository.existsById(request.getEmail())){
            throw new DuplicateException(textProvider.get("error.personalData.duplicate"));
        }
        PersonalData personalData = mapToPersonalData(request, role);
        personalData.setPassword(null);
        personalData.setSetupToken(UUID.randomUUID().toString());
        PersonalData savedPersonalData = personalDataRepository.save(personalData);
        emailService.sendEmailWithTokenPassword(personalData.getEmail(), frontendBaseUrl + "/password/" + personalData.getSetupToken());
        return savedPersonalData;
    }

    /**
     * Sets the password for user using setup token.
     * The token is invalidated after successful password setup.
     */
    @Transactional
    public void setPassword(PasswordRequest request) {
       PersonalData personalData = personalDataRepository.findBySetupToken(request.getToken())
                .orElseThrow(() -> new NotFoundException(textProvider.get("error.token.notFound")));

       String password = passwordEncoder.encode(request.getPassword());
       personalData.setPassword(password);
       personalData.setSetupToken(null);
       personalDataRepository.save(personalData);
       emailService.sendEmailSuccessfulPasswordSetUp(personalData.getEmail());
    }

    private PersonalData mapToPersonalData(PersonalDataRequest request, Role role) {
        PersonalData personalData = new PersonalData();
        personalData.setEmail(request.getEmail());
        personalData.setFirstName(request.getFirstName());
        personalData.setLastName(request.getLastName());
        personalData.setRole(role);
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
