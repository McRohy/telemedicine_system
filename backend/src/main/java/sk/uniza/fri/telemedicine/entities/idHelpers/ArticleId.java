package sk.uniza.fri.telemedicine.entities.idHelpers;

import lombok.Data;
import sk.uniza.fri.telemedicine.entities.Doctor;
import java.time.LocalDateTime;

@Data
public class ArticleId {
    private LocalDateTime date;
    private Doctor doctor;

}
