package sk.uniza.fri.telemedicine.entities;

import jakarta.persistence.*;

@Entity
public class Patient {

    @Id
    private Integer personalNumber;

    @OneToOne
    @JoinColumn(name = "email", nullable = false)
    private PersonalData personalData;

    @OneToOne
    @JoinColumn(name = "care_provider", nullable = false)
    private Doctor doctor;
}
