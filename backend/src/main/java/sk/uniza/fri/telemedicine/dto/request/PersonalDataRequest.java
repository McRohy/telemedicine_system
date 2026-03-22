package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersonalDataRequest {
    @Email
    @NotBlank(message = "Email is mandatory")
    @Size(max = 70, message = "Email must be only 70 characters long")
    private String email;

    @NotBlank(message = "First name is mandatory")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(max = 50)
    private String lastName;
}