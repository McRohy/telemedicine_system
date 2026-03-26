package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.Specialization;

/**
 * Response DTO containing doctor details.
 */
@Getter //for Json serialization
@AllArgsConstructor
public class DoctorResponse {
        private String panNumber;
        private PersonalDataResponse personalData;
        private Specialization specialization;
}
