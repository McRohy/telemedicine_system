package sk.uniza.fri.telemedicine.services.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.LoginRequest;
import sk.uniza.fri.telemedicine.dto.response.LoginResponse;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.enums.constrains.Role;
import sk.uniza.fri.telemedicine.helpers.security.JwtUtils;
import sk.uniza.fri.telemedicine.services.core.DoctorService;
import sk.uniza.fri.telemedicine.services.core.PatientService;
import sk.uniza.fri.telemedicine.services.core.PersonalDataService;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final PersonalDataService personalDataService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    public AuthService(AuthenticationManager authManager, JwtUtils jwtUtils, PersonalDataService personalDataService
    , PatientService patientService, DoctorService doctorService) {
        this.personalDataService = personalDataService;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    public LoginResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        PersonalData pd = personalDataService.getByEmail(request.getEmail());
        String identification = this.getNumber(pd);

        return new LoginResponse(
                jwtUtils.generateToken(request.getEmail()),
                pd.getFirstName(),
                pd.getLastName(),
                pd.getRole(),
                identification

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
