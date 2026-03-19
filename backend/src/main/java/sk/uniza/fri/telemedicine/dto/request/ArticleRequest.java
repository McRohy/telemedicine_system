package sk.uniza.fri.telemedicine.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ArticleRequest {

    @NotBlank(message = "Personal number is mandatory")
    @Pattern(regexp = "\\d{16}", message = "PAN number must consist from 16 digits")
    private String panNumber;

    @NotBlank(message = "Title is mandatory")
    @Size(max=220)
    private String title;

    @NotBlank(message = "Content is mandatory")
    private String content;
}
