package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientResponse {
    private String personalNumber;
    private PersonalDataResponse personalData;
    private String doctorPanNumber;
}
