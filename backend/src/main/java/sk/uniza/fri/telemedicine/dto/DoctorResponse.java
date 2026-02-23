package sk.uniza.fri.telemedicine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter //for Json serialization
@AllArgsConstructor
public class DoctorResponse {
        PersonalDataResponse personalData;
        private String specialization;
}
