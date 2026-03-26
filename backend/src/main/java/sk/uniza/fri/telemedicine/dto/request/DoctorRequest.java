package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import sk.uniza.fri.telemedicine.enums.Specialization;

/**
 * Request DTO for creating a doctor.
 */
@Getter @AllArgsConstructor
public class DoctorRequest {

    @NotBlank(message = "Pan number is mandatory")
    @Pattern(regexp = "\\d{16}", message = "PAN number must consist from 16 digits")
    private String panNumber;

    @Valid
    @NotNull(message = "Personal data is mandatory")
    private PersonalDataRequest personalData;

    @NotNull(message = "Specialization is mandatory")
    private Specialization specialization;
}
