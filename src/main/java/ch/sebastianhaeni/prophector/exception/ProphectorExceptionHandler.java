package ch.sebastianhaeni.prophector.exception;

import ch.sebastianhaeni.prophector.dto.ErrorResponseDto;
import ch.sebastianhaeni.prophector.exception.ProphectorException;
import ch.sebastianhaeni.prophector.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ProphectorExceptionHandler {

    @ExceptionHandler(ProphectorException.class)
    public ResponseEntity<ErrorResponseDto> handleDashboardException(ProphectorException e) {
        if (e instanceof UnauthorizedException) {
            log.info("Unauthorized access by user", e);
        } else {
            log.error("An error in Dashboard Backend occurred ", e);
        }
        var response = new ErrorResponseDto(e.getMessage());
        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

}
