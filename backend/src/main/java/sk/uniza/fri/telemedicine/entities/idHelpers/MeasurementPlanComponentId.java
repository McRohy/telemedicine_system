package sk.uniza.fri.telemedicine.entities.idHelpers;

import lombok.Data;
import sk.uniza.fri.telemedicine.entities.MeasurementPlan;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;
import java.io.Serializable;

@Data
public class MeasurementPlanComponentId implements Serializable {
    private MeasurementPlan measurementPlan;
    private TypeOfMeasurement typeOfMeasurement;
}
