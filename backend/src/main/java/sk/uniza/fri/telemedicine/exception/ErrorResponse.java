package sk.uniza.fri.telemedicine.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * Response DTO for error responses returned by the exception handler.
 */
@Getter @AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
    private Map<String, String> fieldErrors;

    public ErrorResponse(int status, String message) {
        this(status, message, null);
    }
}
