package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class MeasurementFrequency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer frequencyId;

    @NotBlank
    @Column(length = 30, nullable = false, unique = true)
    private String description;

    @NotNull
    @Column(nullable = false)
    private Integer interval;
}
