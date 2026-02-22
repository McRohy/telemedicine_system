package sk.uniza.fri.telemedicine.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersonalDataResponse {
    @Email
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
