package sk.uniza.fri.telemedicine.seed;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sk.uniza.fri.telemedicine.entities.Article;
import sk.uniza.fri.telemedicine.entities.Doctor;
import sk.uniza.fri.telemedicine.entities.TypeOfMeasurement;
import sk.uniza.fri.telemedicine.exception.ArticleException;
import sk.uniza.fri.telemedicine.repository.ArticleRepository;
import sk.uniza.fri.telemedicine.repository.DoctorRepository;
import sk.uniza.fri.telemedicine.repository.TypeOfMeasurementRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Inicializácia demo dát pre typy merania a články.
 */
@Component
@Order(2)
public class DataSeeder implements CommandLineRunner {

    @Value("${app.article.storage-path}")
    private String storagePath;
    private final TypeOfMeasurementRepository typeOfMeasurementRepository;
    private final DoctorRepository doctorRepository;
    private final ArticleRepository articleRepository;

    public DataSeeder(TypeOfMeasurementRepository typeOfMeasurementRepository, DoctorRepository doctorRepository, ArticleRepository articleRepository) {
        this.typeOfMeasurementRepository = typeOfMeasurementRepository;
        this.doctorRepository = doctorRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        seedMeasurementTypes();
        seedArticles();
    }

    private void seedMeasurementTypes() {
        if (typeOfMeasurementRepository.count() > 0) return;

        TypeOfMeasurement temperature = new TypeOfMeasurement();
        temperature.setTypeName("Teplota");
        temperature.setUnits("°C");
        temperature.setMinValue(36.0);
        temperature.setMaxValue(36.9);
        typeOfMeasurementRepository.save(temperature);

        TypeOfMeasurement pulse = new TypeOfMeasurement();
        pulse.setTypeName("Pulz");
        pulse.setUnits("bpm");
        pulse.setMinValue(60.0);
        pulse.setMaxValue(100.0);
        typeOfMeasurementRepository.save(pulse);
    }

    private void seedArticles() {
        if (articleRepository.count() > 0) return;

        Doctor doctor = doctorRepository.findById("9032121546798143")
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        try {
            Path rootPath = Paths.get(storagePath);
            if (!Files.exists(rootPath)) {
                Files.createDirectories(rootPath);
            }

            LocalDateTime now = LocalDateTime.now();

           String  content = "Vysoké teploty môžu predstavovat vážne zdravotné riziká, vrátane dehydratácie, úpalu a zhoršenia chronických ochorení. " +
                    "Je dôležité dodržiavať opatrenia na ochranu pred horúčavou, ako je pitie dostatočného množstva vody, " +
                   "vyhýbanie sa priamemu slnečnému žiareniu a nosenie lehkého oblečenia.";

            String fileName = "article_demo.txt";
            Files.writeString(rootPath.resolve(fileName), content);

            Article article = new Article();
            article.setTitle("Zdravotné riziká spojené s vysokou teplotou");
            article.setFilePath(fileName);
            article.setDoctor(doctor);
            article.setTimeOfCreation(now);
            articleRepository.save(article);

        } catch (IOException e) {
            throw new ArticleException("Failed to save article file");
        }
    }
}
