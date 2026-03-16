package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Setter @Getter @NoArgsConstructor
public class TypeOfMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typeId;

    @Column(length = 30, nullable = false, unique = true)
    private String typeName;

    @Column(length = 4, nullable = false)
    private String units;

    @Column( nullable = false)
    private Double minValue;

    @Column(nullable = false)
    private Double maxValue;
}
