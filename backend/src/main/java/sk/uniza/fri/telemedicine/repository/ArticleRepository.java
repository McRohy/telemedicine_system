package sk.uniza.fri.telemedicine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sk.uniza.fri.telemedicine.entities.Article;
import sk.uniza.fri.telemedicine.entities.idHelpers.ArticleId;

public interface ArticleRepository extends JpaRepository<Article, ArticleId> {
}
