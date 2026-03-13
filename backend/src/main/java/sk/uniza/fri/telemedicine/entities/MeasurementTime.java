package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class MeasurementTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer timeId;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private MeasurementPlan plan;

    @Column(nullable = false)
    private LocalTime time;
}
