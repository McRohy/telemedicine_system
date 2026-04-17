package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Request DTO for setting up a new password.
 */
@Getter @AllArgsConstructor
public class PasswordRequest {
    @NotBlank(message = "{validation.token.mandatory}")
    private String token;

    @NotBlank(message = "{validation.password.mandatory}")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{10,}$",
            message = "{validation.password.strength}"
    )
    private String password;
}