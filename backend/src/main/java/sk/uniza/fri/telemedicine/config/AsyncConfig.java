package sk.uniza.fri.telemedicine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Konfigurácia pre asynchrónne spracovanie.
 * Trieda je prázdna lebo defaultné nastavenia anotácie @EnableAsync stačia.
 *
 * vzor: https://www.baeldung.com/spring-async
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
