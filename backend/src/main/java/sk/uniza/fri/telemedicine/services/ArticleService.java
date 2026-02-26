package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.ArticleRequest;
import sk.uniza.fri.telemedicine.dto.response.ArticleResponse;
import sk.uniza.fri.telemedicine.entities.Article;
import sk.uniza.fri.telemedicine.exception.ResourceNotFoundException;
import sk.uniza.fri.telemedicine.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final DoctorService doctorService;

    public ArticleService(ArticleRepository articleRepository, DoctorService doctorService) {
        this.articleRepository = articleRepository;
        this.doctorService = doctorService;
    }

    @Transactional
    public ArticleResponse createArticle(ArticleRequest request) {
        Article article = mapToArticle(request);
        articleRepository.save(article);
        return mapToArticleResponse(article, request.getPanNumber().toString());
    }

    public List<ArticleResponse> findAllArticlesByPanNumber(Integer panNumber) {
        String author = doctorService.getFullNameByPanNumber(panNumber);
        return articleRepository.findAllByDoctorPanNumber(panNumber)
                .stream()
                .map(article -> mapToArticleResponse(article, author))
                .toList();
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        articleRepository.delete(article);
    }

    private Article mapToArticle(ArticleRequest request) {
        Article article = new Article();
        article.setDate(LocalDateTime.now());
        article.setDoctor(doctorService.findByPanNumber(request.getPanNumber()));
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        return article;
    }

    private ArticleResponse mapToArticleResponse(Article article, String author) {
        return new ArticleResponse(article.getId(), article.getDate(), author, article.getTitle(), article.getContent());
    }
}