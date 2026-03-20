package sk.uniza.fri.telemedicine.services.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.LoginRequest;
import sk.uniza.fri.telemedicine.dto.response.LoginResponse;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.enums.Role;
import sk.uniza.fri.telemedicine.security.JwtUtils;
import sk.uniza.fri.telemedicine.services.core.DoctorService;
import sk.uniza.fri.telemedicine.services.core.PatientService;
import sk.uniza.fri.telemedicine.services.core.PersonalDataService;


/**
 * Service for user authentication of users.
 * It  provides verification of credentials, read user details
 * and generates JWT token with user information.
 */
@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final PersonalDataService personalDataService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public AuthService(AuthenticationManager authManager, JwtUtils jwtUtils,
                       PersonalDataService personalDataService, PatientService patientService,
                       DoctorService doctorService) {
        this.personalDataService = personalDataService;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    /**
     * Verifies user credentials and returns a JWT token with user details.
     * It uses Spring Security's AuthenticationManager to check the email and password.
     */
    public LoginResponse login(LoginRequest request) {
        verifyCredentials(request.getEmail(), request.getPassword());
        PersonalData pd = personalDataService.getByEmail(request.getEmail());

        return new LoginResponse(
                jwtUtils.generateToken(request.getEmail(), "ROLE_" + pd.getRole().name()),  //create cards with email and role
                pd.getFirstName(),
                pd.getLastName(),
                pd.getRole(),
                getNumber(pd)
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
            return patientService.getPatientPersonalNumberByEmail(pd.getEmail());
        }
        if (pd.getRole() == Role.DOCTOR) {
            return doctorService.getPanNumberByEmail(pd.getEmail());
        }
        return null;
    }
}
