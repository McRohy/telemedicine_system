package sk.uniza.fri.telemedicine.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientRequest {
    @NotNull
    private Integer personalNumber;

    @Valid
    @NotNull
    private PersonalDataRequest personalData;

    @NotNull
    private Integer panNumber;
}
