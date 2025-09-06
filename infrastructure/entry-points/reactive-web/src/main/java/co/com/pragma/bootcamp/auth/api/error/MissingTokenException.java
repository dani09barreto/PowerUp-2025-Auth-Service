package co.com.pragma.bootcamp.auth.api.error;

import org.springframework.security.core.AuthenticationException;

public class MissingTokenException extends AuthenticationException {
    public MissingTokenException(String message) {
        super(message);
    }
}