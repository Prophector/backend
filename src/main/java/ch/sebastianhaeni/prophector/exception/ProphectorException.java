package ch.sebastianhaeni.prophector.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProphectorException extends RuntimeException {

    private final HttpStatus statusCode;

    public ProphectorException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ProphectorException(String message, Throwable cause, HttpStatus statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
