package sk.uniza.fri.telemedicine.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientResponse {
    @NotNull
    private PersonalDataResponse personalData;

    @NotNull
    private Integer doctorPanNumber;
}
