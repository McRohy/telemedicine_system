package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Request DTO for personal data used in patient and doctor creation.
 */
@Getter @AllArgsConstructor
public class PersonalDataRequest {
    @Email(message = "{validation.email.format}")
    @NotBlank(message = "{validation.email.mandatory}")
    @Size(max = 70, message = "{validation.email.size}")
    private String email;

    @NotBlank(message = "{validation.firstName.mandatory}")
    @Size(max = 50, message = "{validation.firstName.size}")
    private String firstName;

    @NotBlank(message = "{validation.lastName.mandatory}")
    @Size(max = 50, message = "{validation.lastName.size}")
    private String lastName;
}