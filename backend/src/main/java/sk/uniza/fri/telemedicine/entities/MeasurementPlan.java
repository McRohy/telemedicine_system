package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.enums.Frequency;

import java.time.LocalTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class MeasurementPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    @OneToOne
    @JoinColumn(name = "personal_number", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "pan_number", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Frequency frequency;

    @NotNull
    private LocalTime timeOfPlannedMeasurement;
}
