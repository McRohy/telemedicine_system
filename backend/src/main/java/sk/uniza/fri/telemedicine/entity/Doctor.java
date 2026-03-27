package sk.uniza.fri.telemedicine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.enumeration.Specialization;

@Entity
@Setter @Getter @NoArgsConstructor
public class Doctor {

    @Id
    @Column(length = 16)
    private String panNumber;

    @OneToOne
    @JoinColumn(name = "email", nullable = false)
    private PersonalData personalData;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Specialization specialization;
}
