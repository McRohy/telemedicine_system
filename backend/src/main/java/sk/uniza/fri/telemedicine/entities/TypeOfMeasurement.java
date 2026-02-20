package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class TypeOfMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String typeName;

    @NotBlank
    @Column(length = 4, nullable = false)
    private Character unit;

    @NotNull
    @Column( nullable = false)
    private Integer minValue;

    @NotNull
    @Column(nullable = false)
    private Integer maxValue;
}
