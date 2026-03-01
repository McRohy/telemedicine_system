package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import sk.uniza.fri.telemedicine.dto.request.PasswordRequest;
import sk.uniza.fri.telemedicine.services.PersonalDataService;

@RestController
public class PersonalDataController {

    private final PersonalDataService personalDataService;

    public PersonalDataController(PersonalDataService personalDataService) {
            this.personalDataService = personalDataService;
    }

    @PostMapping("/api/set-password")
    @ResponseStatus(HttpStatus.OK)
    public void setPassword(@Valid @RequestBody PasswordRequest request) {
       personalDataService.setPassword(request);
    }

}
