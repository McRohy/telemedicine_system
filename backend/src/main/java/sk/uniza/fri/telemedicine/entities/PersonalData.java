package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.enums.constrains.Role;

@Entity
@Setter @Getter @NoArgsConstructor
public class PersonalData {

    @Id
    @Email
    private String email;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String lastName;

    @NotBlank
    @Size(max = 30)
    @Column(length = 30, nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Role role;
}

