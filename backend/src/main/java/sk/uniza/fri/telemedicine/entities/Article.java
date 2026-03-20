package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto-increment ID
    private Long articleId;

    @Column(nullable = false)
    private LocalDateTime timeOfCreation;

    @ManyToOne
    @JoinColumn(name = "pan_number", nullable = false)
    private Doctor doctor;

    @Column(length = 220, nullable = false)
    private String title;

    @Column(nullable = false)
    private String filePath;
}