package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;

@Entity
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
