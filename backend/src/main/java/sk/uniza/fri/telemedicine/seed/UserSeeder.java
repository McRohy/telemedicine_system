package sk.uniza.fri.telemedicine.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import sk.uniza.fri.telemedicine.entity.Doctor;
import sk.uniza.fri.telemedicine.entity.Patient;
import sk.uniza.fri.telemedicine.entity.PersonalData;
import sk.uniza.fri.telemedicine.enumeration.Gender;
import sk.uniza.fri.telemedicine.enumeration.Specialization;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;
import sk.uniza.fri.telemedicine.repository.PatientRepository;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;

/**
 * Inserts default data when the application starts.
 * Runs  first (Order 1) and only adds data if the tables are empty.
 * Seeds one admin,  doctor and patient.
 * pattern: https://www.baeldung.com/spring-boot-data-initialization
 */
@Component
@Order(1)
public class UserSeeder implements CommandLineRunner {

    private final PersonalDataRepository personalDataRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(PersonalDataRepository personalDataRepository, DoctorRepository doctorRepository,
                       PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.personalDataRepository = personalDataRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Runs data seeding at application startup.
     */
    @Override
    public void run(String... args) {
        seedUsers();
    }

    private void seedUsers() {
        if (personalDataRepository.count() > 0) return;

        PersonalData pdAdmin = new PersonalData();
        pdAdmin.setFirstName("Matej");
        pdAdmin.setLastName("Adminovský");
        pdAdmin.setEmail("admin@mediroh.sk");
        pdAdmin.setPassword(passwordEncoder.encode("medirohAdmin"));
        pdAdmin.setRole(sk.uniza.fri.telemedicine.enumeration.Role.ADMIN);
        personalDataRepository.save(pdAdmin);

        PersonalData pdDoctor = new PersonalData();
        pdDoctor.setFirstName("Lubos");
        pdDoctor.setLastName("Bohaty");
        pdDoctor.setEmail("doctor@mediroh.sk");
        pdDoctor.setPassword(passwordEncoder.encode("medirohDoctor"));
        pdDoctor.setRole(sk.uniza.fri.telemedicine.enumeration.Role.DOCTOR);
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
        pdPatient.setRole(sk.uniza.fri.telemedicine.enumeration.Role.PATIENT);
        personalDataRepository.save(pdPatient);

        Patient patient = new Patient();
        patient.setPersonalData(pdPatient);
        patient.setPersonalNumber("0404311234");
        patient.setDoctor(doctor);
        patient.setGender(Gender.FEMALE);
        patientRepository.save(patient);

        PersonalData pdDoctorSecond = new PersonalData();
        pdDoctorSecond.setFirstName("Jan");
        pdDoctorSecond.setLastName("Rohaty");
        pdDoctorSecond.setEmail("doctor2@mediroh.sk");
        pdDoctorSecond.setPassword(passwordEncoder.encode("medirohDoctor"));
        pdDoctorSecond.setRole(sk.uniza.fri.telemedicine.enumeration.Role.DOCTOR);
        personalDataRepository.save(pdDoctorSecond);

        Doctor doctorSecond = new Doctor();
        doctorSecond.setPanNumber("9044121546798144");
        doctorSecond.setPersonalData(pdDoctorSecond);
        doctorSecond.setSpecialization(Specialization.CARDIOLOGIST);
        doctorRepository.save(doctorSecond);
    }
}
