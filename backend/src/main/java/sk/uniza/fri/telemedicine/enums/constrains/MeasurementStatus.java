package sk.uniza.fri.telemedicine.enums.constrains;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum MeasurementStatus {
    NORMAL("meranie je v norme"),
    ABNORMAL("meranie je mimo normy, lekár bol informovany notifikaciou"),;

    private final String description;
}
