package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.enums.constrains.Gender;

@Entity
@Setter @Getter @NoArgsConstructor
public class Patient {

    @Id
    @Column(length = 10)
    private String personalNumber;

    @OneToOne
    @JoinColumn(name = "email", nullable = false)
    private PersonalData personalData;

    @ManyToOne
    @JoinColumn(name = "care_provider", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(length = 15, nullable = false)
    private Gender gender;
}
