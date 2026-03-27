package sk.uniza.fri.telemedicine.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Response DTO containing article details.
 */
@Getter @AllArgsConstructor
public class ArticleResponse {
    private Long id;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime timeOfCreation;
    private String title;
    private String content;
}