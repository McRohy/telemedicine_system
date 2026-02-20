package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;
}
