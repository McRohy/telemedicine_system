package sk.uniza.fri.telemedicine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersonalDataResponse {
    private String email;
    private String firstName;
    private String lastName;
}
