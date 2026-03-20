package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.enums.MeasurementStatus;
import java.time.LocalDateTime;

@Entity
@Setter @Getter @NoArgsConstructor
public class MeasurementRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "type_of_measurement_id", nullable = false)
    private TypeOfMeasurement typeOfMeasurement;

    @ManyToOne
    @JoinColumn(name = "personal_number", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime timeOfMeasurement;

    @Column(nullable = false)
    private Double value;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MeasurementStatus measurementStatus;

    @Column(length = 220)
    private String note;
}
