package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.PersonalDataRequest;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.enums.Role;
import sk.uniza.fri.telemedicine.exception.DuplicateException;
import sk.uniza.fri.telemedicine.helpers.EmailSender;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

import java.util.Random;

@Service
public class PersonalDataService {

    private final PersonalDataRepository personalDataRepository;
    private final EmailSender emailSender;

    public PersonalDataService(PersonalDataRepository personalDataRepository, EmailSender emailSender) {
        this.personalDataRepository = personalDataRepository;
        this.emailSender = emailSender;
    }

    @Transactional
    public PersonalData createPersonalData(PersonalDataRequest request) {
        if (personalDataRepository.existsById(request.getEmail())){
            throw new DuplicateException("Personal data with this email already exists");
        }

        PersonalData personalData = new PersonalData();
        personalData.setEmail(request.getEmail());
        personalData.setFirstName(request.getFirstName());
        personalData.setLastName(request.getLastName());
        personalData.setRole(Role.valueOf(request.getRole().toUpperCase()));

        String temporary_password = generateRandomPassword();
        personalData.setPassword(generateRandomPassword());
        emailSender.sendEmailWithPassword(request.getEmail(), temporary_password);

        return personalDataRepository.save(personalData);
    }

    private String generateRandomPassword() {
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                password.append((char) (random.nextInt(26) + 'a'));
            } else {
                password.append(random.nextInt(10));
            }
        }
        return password.toString();
    }
}
