package ch.sebastianhaeni.prophector.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ProphectorException {

    public UnauthorizedException() {
        super("User not authorized to execute this action.", HttpStatus.UNAUTHORIZED);
    }
}
