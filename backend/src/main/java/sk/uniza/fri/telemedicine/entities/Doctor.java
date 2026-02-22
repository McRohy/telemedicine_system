package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.enums.Specialization;

@Entity
@Setter @Getter @NoArgsConstructor
public class Doctor {

    @Id
    private Integer PanNumber;

    @OneToOne
    @JoinColumn(name = "email", nullable = false)
    private PersonalData personalData;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Specialization specialization;
}
