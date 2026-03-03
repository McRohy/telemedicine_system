package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientRequest {
    @NotBlank(message = "Personal number is mandatory")
    private String personalNumber;

    @Valid
    @NotNull(message = "Personal data is mandatory")
    private PersonalDataRequest personalData;

    @NotBlank(message = "Pan number is mandatory")
    private String panNumber;
}
