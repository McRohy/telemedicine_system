package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enumeration.Specialization;

/**
 * Request DTO for creating a doctor.
 */
@Getter @AllArgsConstructor
public class DoctorRequest {

    @NotBlank(message = "{validation.pan.mandatory}")
    @Pattern(regexp = "\\d{16}", message = "{validation.pan.format}")
    private String panNumber;

    @Valid
    @NotNull(message = "{validation.personalData.mandatory}")
    private PersonalDataRequest personalData;

    @NotNull(message = "{validation.specialization.mandatory}")
    private Specialization specialization;
}
