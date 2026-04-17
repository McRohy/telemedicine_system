package sk.uniza.fri.telemedicine.config;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


import java.util.Locale;

/**
 * Provides localized texts from messages.properties.
 */
@Component
public class TextProvider {

    private final MessageSource messageSource;

    public TextProvider(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Finds text in messages.properties by key
     * and fills placeholders {0}, {1}, ... with the given args.
     */
    public String get(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}
