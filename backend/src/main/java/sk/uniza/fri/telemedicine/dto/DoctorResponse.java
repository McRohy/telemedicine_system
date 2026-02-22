package sk.uniza.fri.telemedicine.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter //for Json serialization
@AllArgsConstructor
public class DoctorResponse {
        @NotNull
        PersonalDataResponse personalData;

        @NotBlank
        private String specialization;
}
