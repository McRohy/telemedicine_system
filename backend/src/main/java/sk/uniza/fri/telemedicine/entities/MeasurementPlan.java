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

    @ManyToOne
    @JoinColumn(name = "id_frequency", nullable = false)
    private MeasurementFrequency measurementFrequency;
}
