package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sk.uniza.fri.telemedicine.enums.Frequency;

import java.time.LocalDateTime;

@Entity
@Getter @Setter @NoArgsConstructor
public class MeasurementPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer planId;

    @OneToOne
    @JoinColumn(name = "personal_number", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "pan_number", nullable = false)
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private Frequency frequency;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastUpdateAt;
}
