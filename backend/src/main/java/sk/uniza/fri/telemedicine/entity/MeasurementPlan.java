package sk.uniza.fri.telemedicine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.enumeration.Frequency;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class MeasurementPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @ManyToOne
    @JoinColumn(name = "personal_number", nullable = false)
    private Patient patient;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Frequency frequency;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    private LocalDateTime validTo;
}
