package sk.uniza.fri.telemedicine.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import sk.uniza.fri.telemedicine.dto.request.ArticleRequest;
import sk.uniza.fri.telemedicine.dto.response.ArticleResponse;
import sk.uniza.fri.telemedicine.services.ArticleService;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ArticleResponse createArticle(@Valid @RequestBody ArticleRequest request) {
        return articleService.createArticle(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ArticleResponse> findAllArticlesByPanNumber(@RequestParam String panNumber) {
        return articleService.findAllArticlesByPanNumber(panNumber);
    }

    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    public ArticleResponse  getArticleById(@PathVariable Long articleId) {
        return articleService.findArticleById(articleId);
    }

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
    }
}