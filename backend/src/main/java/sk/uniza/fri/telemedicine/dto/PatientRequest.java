package sk.uniza.fri.telemedicine.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PatientRequest {
    @NotNull
    private Integer personalNumber;

    @Valid
    @NotNull
    private PersonalDataRequest personalData;

    @NotNull
    private Integer panNumber;
}
