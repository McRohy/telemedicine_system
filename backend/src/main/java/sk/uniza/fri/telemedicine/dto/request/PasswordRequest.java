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
    @NotBlank
    private String token;

    @NotBlank(message = "Password is mandatory")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{10,}$",
            message = "Password must be at least 10 characters long, 1 uppercase letter, 1 lowercase letter and 1 number"
    )
    private String password;
}