package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import sk.uniza.fri.telemedicine.entities.idHelpers.MeasurementPlanComponentId;
import java.time.LocalTime;

@Entity
@IdClass(MeasurementPlanComponentId.class)
public class MeasurementPlanComponent {

    @Id
    @ManyToOne
    @JoinColumn(name = "type_of_measurement_id")
    private TypeOfMeasurement typeOfMeasurement;

    @Id
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private MeasurementPlan measurementPlan;

    @NotNull
    @Column(nullable = false)
    private LocalTime timeOfPlannedMeasurement;

}
