package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class MeasurementTimePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeId;

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private MeasurementPlan measurementPlan;

    @Column(nullable = false)
    private LocalTime time;
}
