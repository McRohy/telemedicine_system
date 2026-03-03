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
public class DoctorRequest {

    @NotBlank(message = "Personal number is mandatory")
    @Pattern(regexp = "\\d{16}", message = "PAN number must consist from 16 digits")
    private String panNumber;

    @Valid
    @NotNull(message = "Personal data is mandatory")
    private PersonalDataRequest personalData;

    @NotBlank(message = "Specialization is mandatory")
    @Size(max=20)
    private String specialization;
}
