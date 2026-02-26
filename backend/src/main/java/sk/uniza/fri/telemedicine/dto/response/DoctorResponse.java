package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter //for Json serialization
@AllArgsConstructor
public class DoctorResponse {
        private Integer panNumber;
        private PersonalDataResponse personalData;
        private String specialization;
}
