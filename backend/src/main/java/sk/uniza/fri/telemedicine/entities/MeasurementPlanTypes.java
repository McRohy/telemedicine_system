package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.entities.idHelpers.MeasurementPlanComponentId;
import java.time.LocalTime;

@Entity
@IdClass(MeasurementPlanComponentId.class)
@Getter @Setter @NoArgsConstructor
public class MeasurementPlanTypes {

    @Id
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private MeasurementPlan measurementPlan;

    @Id
    @ManyToOne
    @JoinColumn(name = "type_of_measurement_id")
    private TypeOfMeasurement typeOfMeasurement;
}
