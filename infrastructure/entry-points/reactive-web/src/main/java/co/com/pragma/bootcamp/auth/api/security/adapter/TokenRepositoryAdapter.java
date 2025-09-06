package co.com.pragma.bootcamp.auth.api.security.adapter;

import co.com.pragma.bootcamp.auth.api.security.jwt.provider.JwtProvider;
import co.com.pragma.bootcamp.auth.model.token.gateways.TokenRepository;
import co.com.pragma.bootcamp.auth.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TokenRepositoryAdapter implements TokenRepository {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    @Override
    public String generateToken(User user) {
        return jwtProvider.generateToken(
                // Creating a UserDetails object on the fly
                new UserDetails() {
                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return Stream.of(user.getRole().getName().split(", ")).map(SimpleGrantedAuthority::new)
                                .toList();
                    }

                    @Override
                    public String getPassword() {
                        return user.getPassword();
                    }

                    @Override
                    public String getUsername() {
                        return user.getEmail();
                    }
                }
        );
    }

    @Override
    public String validateToken(String token) {
        return "";
    }

    @Override
    public Boolean passwordEncoderMatches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
