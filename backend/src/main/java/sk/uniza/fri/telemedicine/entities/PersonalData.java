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

    @Column(length = 50, nullable = false)
    private String firstName;

    @Column(length = 50, nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Role role;

    @Column(length = 60)
    private String password;

    private String setupToken;

}

