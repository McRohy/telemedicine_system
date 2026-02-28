package sk.uniza.fri.telemedicine.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sk.uniza.fri.telemedicine.dto.request.ArticleRequest;
import sk.uniza.fri.telemedicine.dto.response.ArticleResponse;
import sk.uniza.fri.telemedicine.entities.Article;
import sk.uniza.fri.telemedicine.entities.Doctor;
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
        Doctor doctor = doctorService.findByPanNumber(request.getPanNumber());
        Article article = mapToArticle(request, doctor);
        articleRepository.save(article);
        String author = doctorService.getFullNameByPanNumber(request.getPanNumber());
        return mapToArticleResponse(article, author);
    }

    public List<ArticleResponse> findAllArticlesByPanNumber(String panNumber) {
        String author = doctorService.getFullNameByPanNumber(panNumber);
        return articleRepository.findAllByDoctorPanNumber(panNumber)
                .stream()
                .map(article -> mapToArticleResponse(article, author))
                .toList();
    }

    public ArticleResponse findArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        String author = doctorService.getFullNameByPanNumber(article.getDoctor().getPanNumber());
        return this.mapToArticleResponse(article, author);
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new ResourceNotFoundException("Article not found"));
        articleRepository.delete(article);
    }

    private Article mapToArticle(ArticleRequest request, Doctor doctor) {
        Article article = new Article();
        article.setDate(LocalDateTime.now());
        article.setDoctor(doctor);
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        return article;
    }

    private ArticleResponse mapToArticleResponse(Article article, String author) {
        return new ArticleResponse(article.getId(), article.getDate(), author, article.getTitle(), article.getContent());
    }
}