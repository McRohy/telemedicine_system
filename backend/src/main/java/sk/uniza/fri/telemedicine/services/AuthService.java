package sk.uniza.fri.telemedicine.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.LoginRequest;
import sk.uniza.fri.telemedicine.dto.response.LoginResponse;
import sk.uniza.fri.telemedicine.entities.PersonalData;
import sk.uniza.fri.telemedicine.helpers.security.JwtUtils;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final PersonalDataService personalDataService;

    public AuthService(AuthenticationManager authManager, JwtUtils jwtUtils, PersonalDataService personalDataService) {
        this.personalDataService = personalDataService;
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        PersonalData pd = personalDataService.getByEmail(request.getEmail());

        return new LoginResponse(
                jwtUtils.generateToken(request.getEmail()),
                pd.getFirstName(),
                pd.getLastName(),
                pd.getRole()
        );
    }
}
