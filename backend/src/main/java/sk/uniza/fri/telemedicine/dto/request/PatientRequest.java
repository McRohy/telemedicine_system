package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.constrains.Gender;

@Getter
@AllArgsConstructor
public class PatientRequest {
    @NotBlank(message = "Personal number is mandatory")
    @Pattern(regexp = "\\d{10}", message = "Personal number must consist from 10 digits")
    private String personalNumber;

    @Valid
    @NotNull(message = "Personal data is mandatory")
    private PersonalDataRequest personalData;

    @NotBlank(message = "Pan number is mandatory")
    @Pattern(regexp = "\\d{16}", message = "PAN number must consist from 16 digits")
    private String panNumber;

    @NotNull(message = "Gender is mandatory")
    private Gender gender; //jackson will deserialize
}
