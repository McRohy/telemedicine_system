package sk.uniza.fri.telemedicine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleResponse {
    private Integer id;
    private LocalDateTime timeOfCreation;
    private String author;
    private String title;
    private String content;
}