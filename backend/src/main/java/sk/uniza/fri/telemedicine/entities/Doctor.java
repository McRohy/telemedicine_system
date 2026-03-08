package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.enums.constrains.Specialization;

@Entity
@Setter @Getter @NoArgsConstructor
public class Doctor {

    @Id
    @Column(length = 16)
    private String PanNumber;

    @OneToOne
    @JoinColumn(name = "email", nullable = false)
    private PersonalData personalData;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Specialization specialization;
}
