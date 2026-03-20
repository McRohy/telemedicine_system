package sk.uniza.fri.telemedicine.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import sk.uniza.fri.telemedicine.entities.Doctor;
import sk.uniza.fri.telemedicine.entities.Patient;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.enums.Gender;
import sk.uniza.fri.telemedicine.enums.Specialization;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;
import sk.uniza.fri.telemedicine.repository.PatientRepository;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

/**
 * Inicializácia demo dát používateľov.
 * Vzor: https://www.javapro.academy/loading-initial-data-with-spring-boot/
 */
@Component
@Order(1)
public class UserSeeder implements CommandLineRunner {

    private final PersonalDataRepository personalDataRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    private UserSeeder(PersonalDataRepository personalDataRepository, DoctorRepository doctorRepository,
                       PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.personalDataRepository = personalDataRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Spustí inicializáciu používateľov pri štarte aplikácie.
     */
    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    private void seedUsers() {
        if (personalDataRepository.count() > 0) return;

        PersonalData pdAdmin = new PersonalData();
        pdAdmin.setFirstName("Matej");
        pdAdmin.setLastName("Adminovský");
        pdAdmin.setEmail("admin@mediroh.sk");
        pdAdmin.setPassword(passwordEncoder.encode("medirohAdmin"));
        pdAdmin.setRole(sk.uniza.fri.telemedicine.enums.Role.ADMIN);
        personalDataRepository.save(pdAdmin);

        PersonalData pdDoctor = new PersonalData();
        pdDoctor.setFirstName("Lubos");
        pdDoctor.setLastName("Roh");
        pdDoctor.setEmail("doctor@mediroh.sk");
        pdDoctor.setPassword(passwordEncoder.encode("medirohDoctor"));
        pdDoctor.setRole(sk.uniza.fri.telemedicine.enums.Role.DOCTOR);
        personalDataRepository.save(pdDoctor);

        Doctor doctor = new Doctor();
        doctor.setPanNumber("9032121546798143");
        doctor.setPersonalData(pdDoctor);
        doctor.setSpecialization(Specialization.CARDIOLOGIST);
        doctorRepository.save(doctor);

        PersonalData pdPatient = new PersonalData();
        pdPatient.setFirstName("Emma");
        pdPatient.setLastName("Roh");
        pdPatient.setEmail("roh@gmail.com");
        pdPatient.setPassword(passwordEncoder.encode("mediroh"));
        pdPatient.setRole(sk.uniza.fri.telemedicine.enums.Role.PATIENT);
        personalDataRepository.save(pdPatient);

        Patient patient = new Patient();
        patient.setPersonalData(pdPatient);
        patient.setPersonalNumber("0404311234");
        patient.setDoctor(doctor);
        patient.setGender(Gender.FEMALE);
        patientRepository.save(patient);
    }
}
