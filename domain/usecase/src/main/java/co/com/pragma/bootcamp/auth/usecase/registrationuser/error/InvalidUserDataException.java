package co.com.pragma.bootcamp.auth.usecase.registrationuser.error;

public class InvalidUserDataException extends RuntimeException {

    public InvalidUserDataException(String message) {
        super(message);
    }
}
