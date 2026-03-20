package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class PasswordRequest {
    @NotBlank
    private String token;
    @NotBlank(message = "Password is mandatory")
    private String password;
}