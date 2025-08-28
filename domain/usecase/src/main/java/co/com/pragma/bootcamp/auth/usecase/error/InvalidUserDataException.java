package co.com.pragma.bootcamp.auth.usecase.error;

public class InvalidUserDataException extends RuntimeException {

    public InvalidUserDataException(String message) {
        super(message);
    }
}
