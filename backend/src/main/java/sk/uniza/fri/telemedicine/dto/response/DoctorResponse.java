package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.constrains.Specialization;

@Getter //for Json serialization
@AllArgsConstructor
public class DoctorResponse {
        private Integer panNumber;
        private PersonalDataResponse personalData;
        private Specialization specialization;
}
