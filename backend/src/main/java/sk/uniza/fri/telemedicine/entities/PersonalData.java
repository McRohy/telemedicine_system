package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import sk.uniza.fri.telemedicine.enums.Role;

@Entity
public class PersonalData {

    @Id
    @Email
    private String email;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String firstName;

    @NotBlank
    @Column(length = 50, nullable = false)
    private String lastName;

    @NotBlank
    @Column(length = 30, nullable = false)
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}

