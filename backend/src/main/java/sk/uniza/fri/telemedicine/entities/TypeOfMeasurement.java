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

    @NotBlank
    @Column(length = 30, nullable = false, unique = true)
    private String typeName;

    @NotBlank
    @Size(max = 4)
    @Column(length = 4, nullable = false)
    private String units;

    @NotNull
    @Column( nullable = false)
    private Integer minValue;

    @NotNull
    @Column(nullable = false)
    private Integer maxValue;
}
