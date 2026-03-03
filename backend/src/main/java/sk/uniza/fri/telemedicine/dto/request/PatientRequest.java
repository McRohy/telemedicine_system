package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PatientRequest {
    @NotBlank(message = "Personal number is mandatory")
    @Pattern(regexp = "\\d{11}", message = "Personal number must consist from 11 digits")
    private String personalNumber;

    @Valid
    @NotNull(message = "Personal data is mandatory")
    private PersonalDataRequest personalData;

    @NotBlank(message = "Pan number is mandatory")
    @Pattern(regexp = "\\d{24}", message = "PAN number must consist from 24 digits")
    private String panNumber;
}
