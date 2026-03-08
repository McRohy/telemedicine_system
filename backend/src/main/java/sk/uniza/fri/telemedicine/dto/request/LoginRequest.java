package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class LoginRequest {
    @NotBlank
    private  String email;
    @NotBlank
    private String password;
}
