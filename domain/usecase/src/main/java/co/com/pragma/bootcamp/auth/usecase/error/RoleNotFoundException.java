package co.com.pragma.bootcamp.auth.usecase.error;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
