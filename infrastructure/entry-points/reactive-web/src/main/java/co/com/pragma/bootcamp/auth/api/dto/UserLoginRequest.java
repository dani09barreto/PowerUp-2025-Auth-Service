package co.com.pragma.bootcamp.auth.api.dto;

public record UserLoginRequest(
        String email,
        String password
) {
}
