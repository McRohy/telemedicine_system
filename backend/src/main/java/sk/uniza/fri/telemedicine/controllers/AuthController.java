package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.LoginRequest;
import sk.uniza.fri.telemedicine.dto.request.PasswordRequest;
import sk.uniza.fri.telemedicine.dto.response.LoginResponse;
import sk.uniza.fri.telemedicine.services.AuthService;
import sk.uniza.fri.telemedicine.services.PersonalDataService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PersonalDataService personalDataService;

    public AuthController(AuthService authService, PersonalDataService personalDataService) {
        this.authService = authService;
        this.personalDataService = personalDataService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/password")
    public void setPassword(@Valid @RequestBody PasswordRequest request) {
        personalDataService.setPassword(request);
    }
}
