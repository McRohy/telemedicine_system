package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Request DTO for user login.
 */
@Getter @AllArgsConstructor
public class LoginRequest {
    @Email
    @NotBlank
    private  String email;
    @NotBlank
    private String password;
}
