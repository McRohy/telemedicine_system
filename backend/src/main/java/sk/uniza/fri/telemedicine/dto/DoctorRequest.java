package sk.uniza.fri.telemedicine.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DoctorRequest {
    @Valid @NotNull
    private PersonalDataRequest personalData;

    @NotNull
    private Integer panNumber;

    @NotBlank
    private String specialization;
}
