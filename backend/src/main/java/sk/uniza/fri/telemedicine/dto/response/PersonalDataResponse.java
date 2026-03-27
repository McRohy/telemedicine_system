package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response DTO containing personal data details.
 */
@Getter @AllArgsConstructor
public class PersonalDataResponse {
    private String email;
    private String firstName;
    private String lastName;
}
