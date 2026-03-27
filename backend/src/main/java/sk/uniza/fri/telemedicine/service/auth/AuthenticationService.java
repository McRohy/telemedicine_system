package sk.uniza.fri.telemedicine.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.LoginRequest;
import sk.uniza.fri.telemedicine.dto.response.LoginResponse;
import sk.uniza.fri.telemedicine.entity.PersonalData;
import sk.uniza.fri.telemedicine.enumeration.Role;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;
import sk.uniza.fri.telemedicine.repository.PatientRepository;
import sk.uniza.fri.telemedicine.repository.PersonalDataRepository;
import sk.uniza.fri.telemedicine.security.JwtUtils;

/**
 * Service for user authentication.
 * It provides verification of credentials, read user details
 * and generates JWT token with user information.
 * Uses repositories directly to follow the layer convention.
 */
@Service
public class AuthenticationService {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final PersonalDataRepository personalDataRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AuthenticationService(AuthenticationManager authManager, JwtUtils jwtUtils,
                                 PersonalDataRepository personalDataRepository, PatientRepository patientRepository,
                                 DoctorRepository doctorRepository) {
        this.personalDataRepository = personalDataRepository;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    /**
     * Verifies user credentials and returns a JWT token with user details.
     * It uses Spring Security's AuthenticationManager to check the email and password.
     */
    public LoginResponse login(LoginRequest request) {
        verifyCredentials(request.getEmail(), request.getPassword());
        PersonalData pd = personalDataRepository.findById(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Personal data not found"));

        String identificationNumber = getNumber(pd);
        return new LoginResponse(
                jwtUtils.generateToken(request.getEmail(), "ROLE_" + pd.getRole().name(), identificationNumber),
                pd.getFirstName(),
                pd.getLastName(),
                pd.getRole(),
                identificationNumber
        );
    }

    /**
     * Uses Spring AuthenticationManager to check email and password.
     * Spring does internally: UserAuthenticationService and BCrypt.matches(password, hash).
     * If is not valid, it throws BadCredentialsException which is handled by Spring Security and returns 401.
     */
    private void verifyCredentials(String email, String password) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
    }

    private String getNumber(PersonalData pd) {
        if (pd.getRole() == Role.PATIENT) {
            return patientRepository.findPersonalNumberByEmail(pd.getEmail())
                    .orElseThrow(() -> new NotFoundException("Patient not found"));
        }
        if (pd.getRole() == Role.DOCTOR) {
            return doctorRepository.findPanNumberByEmail(pd.getEmail())
                    .orElseThrow(() -> new NotFoundException("Doctor not found"));
        }
        return null;
    }
}