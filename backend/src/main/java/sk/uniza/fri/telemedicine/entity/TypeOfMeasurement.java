package sk.uniza.fri.telemedicine.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter @Getter @NoArgsConstructor
public class TypeOfMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long typeId;

    @Column(length = 30, nullable = false, unique = true)
    private String typeName;

    @Column(length = 4, nullable = false)
    private String units;

    @Column( nullable = false)
    private Double minValue;

    @Column(nullable = false)
    private Double maxValue;
}
