package co.com.pragma.bootcamp.auth.model.token.gateways;

import co.com.pragma.bootcamp.auth.model.user.User;

public interface TokenRepository {
    String generateToken(User user);
    String validateToken(String token);
    Boolean passwordEncoderMatches(String rawPassword, String encodedPassword);
    String encodePassword(String rawPassword);
}
