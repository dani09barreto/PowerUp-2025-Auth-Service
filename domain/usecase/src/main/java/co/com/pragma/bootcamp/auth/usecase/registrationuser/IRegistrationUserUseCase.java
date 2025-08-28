package co.com.pragma.bootcamp.auth.usecase.registrationuser;

import co.com.pragma.bootcamp.auth.model.user.User;
import reactor.core.publisher.Mono;

public interface IRegistrationUserUseCase {
    Mono<User> registerUser(User user);
    void validateUser(User user) throws IllegalArgumentException;
}
