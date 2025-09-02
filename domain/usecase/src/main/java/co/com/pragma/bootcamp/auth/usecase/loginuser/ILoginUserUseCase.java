package co.com.pragma.bootcamp.auth.usecase.loginuser;

import co.com.pragma.bootcamp.auth.model.token.Token;
import reactor.core.publisher.Mono;

public interface ILoginUserUseCase {
    Mono<Token> login(String email, String password);
}
