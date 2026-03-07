package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.PasswordRequest;
import sk.uniza.fri.telemedicine.services.PersonalDataService;

@RestController
@RequestMapping("api/")
public class PersonalDataController {

    private final PersonalDataService personalDataService;

    public PersonalDataController(PersonalDataService personalDataService) {
            this.personalDataService = personalDataService;
    }

    @PostMapping("/auth/set-password")
    @ResponseStatus(HttpStatus.OK)
    public void setPassword(@Valid @RequestBody PasswordRequest request) {
       personalDataService.setPassword(request);
    }

}
