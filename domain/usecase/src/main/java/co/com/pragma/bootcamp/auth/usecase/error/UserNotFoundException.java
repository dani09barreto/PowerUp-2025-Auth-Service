package co.com.pragma.bootcamp.auth.usecase.error;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
