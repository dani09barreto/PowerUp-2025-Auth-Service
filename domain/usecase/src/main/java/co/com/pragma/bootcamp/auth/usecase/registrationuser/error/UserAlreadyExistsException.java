package co.com.pragma.bootcamp.auth.usecase.registrationuser.error;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
