package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enumeration.Role;

/**
 * Response DTO containing login details and authentication token.
 */
@Getter @AllArgsConstructor
public class LoginResponse {
    private String token;
    private String firstName;
    private String lastName;
    private Role role;
    private String identificationNumber;
}
