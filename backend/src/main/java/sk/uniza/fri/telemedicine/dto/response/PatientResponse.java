package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.Gender;

@Getter
@AllArgsConstructor
public class PatientResponse {
    private String personalNumber;
    private PersonalDataResponse personalData;
    private String doctorPanNumber;
    private Gender gender;
}
