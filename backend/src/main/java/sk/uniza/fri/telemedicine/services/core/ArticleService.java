package sk.uniza.fri.telemedicine.services.core;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sk.uniza.fri.telemedicine.dto.request.ArticleRequest;
import sk.uniza.fri.telemedicine.dto.response.ArticleResponse;
import sk.uniza.fri.telemedicine.entities.Article;
import sk.uniza.fri.telemedicine.exception.ArticleException;
import sk.uniza.fri.telemedicine.exception.NotFoundException;
import sk.uniza.fri.telemedicine.repository.ArticleRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ArticleService {

    //Cesta kam sa ukladajú články, v Dockeri mapovaná na volume "article-data"
    @Value("${app.article.storage-path}")
    private String storagePath;

    private final ArticleRepository articleRepository;
    private final DoctorService doctorService;

    public ArticleService(ArticleRepository articleRepository, DoctorService doctorService) {
        this.articleRepository = articleRepository;
        this.doctorService = doctorService;
    }

    /**
     * Súbor sa uloží na disk (Docker volume), do DB len dáta + filePath
     * Vzor: https://howtodoinjava.com/java11/write-string-to-file/
     * Vzor: https://foojay.io/today/exploring-file-storage-solutions-in-spring-boot-database-local-systems-cloud-services-and-beyond/
     */
    @Transactional
    public ArticleResponse createArticle(ArticleRequest request) {
        Article article = new Article();
        try {
            Path rootPath = Paths.get(storagePath);
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
            }

            LocalDateTime now = LocalDateTime.now();
            String fileName = "article_" + request.getPanNumber() + "_" +
                    now.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";

            article.setTitle(request.getTitle());
            article.setFilePath(fileName);
            article.setDoctor(doctorService.findByPanNumber(request.getPanNumber()));
            article.setTimeOfCreation(now);
            articleRepository.save(article);

            Files.writeString(rootPath.resolve(fileName), request.getContent()); //create file, write , close file in one step

        } catch (IOException e) {
            throw new ArticleException("Failed to save article file");
        }

        return mapToArticleResponse(article, true);
    }

    public Page<ArticleResponse> getAllArticlesByPanNumber(String panNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timeOfCreation").descending());
        return articleRepository.findAllByPanNumber(panNumber, pageable)
                .map(article -> mapToArticleResponse(article, false));
    }

    public Page<ArticleResponse> getAllArticles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timeOfCreation").descending());
        return articleRepository.findAll(pageable)
                .map(article -> mapToArticleResponse(article, false));
    }

    public ArticleResponse getArticleById(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("Article not found"));
        return mapToArticleResponse(article, true);
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundException("Article not found"));

        try {
            Files.deleteIfExists(Paths.get(storagePath, article.getFilePath()));
        } catch (IOException e) {
            throw new ArticleException("Failed to delete article file");
        }
        articleRepository.delete(article);
    }

    private ArticleResponse mapToArticleResponse(Article article, boolean withContent) {
        String content = null;
        if (withContent) {
            try {
                content = Files.readString(Paths.get(storagePath).resolve(article.getFilePath()));
            } catch (IOException e) {
                throw new ArticleException("Failed to read article content");
            }
        }
        return new ArticleResponse(
                article.getArticleId(),
                article.getTimeOfCreation(),
                article.getTitle(),
                content
        );
    }
}