package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import sk.uniza.fri.telemedicine.entities.idHelpers.ArticleId;
import java.time.LocalDateTime;

@Entity
@IdClass(ArticleId.class)
public class Article {

    @Id
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "pan_cislo", nullable = false)
    private Doctor doctor;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String content;
}
