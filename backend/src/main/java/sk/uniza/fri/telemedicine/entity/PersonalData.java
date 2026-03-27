package sk.uniza.fri.telemedicine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.enumeration.Role;

@Entity
@Setter @Getter @NoArgsConstructor
public class PersonalData {

    @Id
    @Column(length = 70)
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

    @Column(length = 36)
    private String setupToken;

}

