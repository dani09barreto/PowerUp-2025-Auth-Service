package co.com.pragma.bootcamp.auth.usecase.loginuser;

import co.com.pragma.bootcamp.auth.model.role.gateways.IRoleRepository;
import co.com.pragma.bootcamp.auth.model.token.Token;
import co.com.pragma.bootcamp.auth.model.token.gateways.TokenRepository;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import co.com.pragma.bootcamp.auth.usecase.error.InvalidUserCredentialsException;
import co.com.pragma.bootcamp.auth.usecase.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUserUseCase implements ILoginUserUseCase{

    private final IUserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final IRoleRepository roleRepository;

    @Override
    public Mono<Token> login(String email, String password) {
        return userRepository.findByEmail(email)
                .flatMap(user -> roleRepository.getRoleById(user.getRole().getId())
                        .map(role -> {
                            user.setRole(role);
                            return user;
                        }))
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with email: " + email)))
                .filter(user -> tokenRepository.passwordEncoderMatches(password, user.getPassword()))
                .map(tokenRepository::generateToken)
                .map(Token::new)
                .log()
                .switchIfEmpty(Mono.error(new InvalidUserCredentialsException("Invalid credentials")));
    }
}
