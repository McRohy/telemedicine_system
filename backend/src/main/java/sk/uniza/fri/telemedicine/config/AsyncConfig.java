package sk.uniza.fri.telemedicine.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 *  Asynchronous configuration class for the application.
 *  Class is empty because the default settings of the @EnableAsync annotation are enough.
 * pattern: https://www.baeldung.com/spring-async
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}
