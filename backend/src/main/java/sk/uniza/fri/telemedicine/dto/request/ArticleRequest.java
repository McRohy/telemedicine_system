package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Request DTO for creating an article.
 */
@Getter @AllArgsConstructor
public class ArticleRequest {

    @NotBlank(message = "{validation.pan.mandatory}")
    @Pattern(regexp = "\\d{16}", message = "{validation.pan.format}")
    private String panNumber;

    @NotBlank(message = "{validation.title.mandatory}")
    @Size(max = 220, message = "{validation.title.size}")
    private String title;

    @NotBlank(message = "{validation.content.mandatory}")
    @Size(max = 50000, message = "{validation.content.size}")
    private String content;
}
