package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class MeasurementTypePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private MeasurementPlan measurementPlan;

    @ManyToOne
    @JoinColumn(name = "measurement_type_id", nullable = false)
    private TypeOfMeasurement typeOfMeasurement;
}
