package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.entities.idHelpers.MeasurementRecordId;

import java.time.LocalDateTime;

@Entity
@IdClass(MeasurementRecordId.class)
@Setter @Getter @NoArgsConstructor
public class MeasurementRecord {

    @Id
    private LocalDateTime timeOfMeasurement;

    @Id
    @ManyToOne
    @JoinColumn(name = "type_of_measurement_id", nullable = false)
    private TypeOfMeasurement typeOfMeasurement;

    @Id
    @ManyToOne
    @JoinColumn(name = "personal_number", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private Integer value;

    @Column(length = 220)
    private String note;
}
