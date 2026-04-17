package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enumeration.Gender;

/**
 * Request DTO for creating a patient.
 */
@Getter @AllArgsConstructor
public class PatientRequest {
    @NotBlank(message = "{validation.personalNumber.mandatory}")
    @Pattern(regexp = "\\d{10}", message = "{validation.personalNumber.format}")
    private String personalNumber;

    @Valid
    @NotNull(message = "{validation.personalData.mandatory}")
    private PersonalDataRequest personalData;

    @NotBlank(message = "{validation.pan.mandatory}")
    @Pattern(regexp = "\\d{16}", message = "{validation.pan.format}")
    private String panNumber;

    @NotNull(message = "{validation.gender.mandatory}")
    private Gender gender; //jackson will deserialize
}
