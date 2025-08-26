package co.com.pragma.bootcamp.auth.usecase.userbyidentification;

import co.com.pragma.bootcamp.auth.model.user.User;
import reactor.core.publisher.Mono;

public interface IUserByIdentificationUseCase {
    Mono<User> getUserByIdentification(String numberIdentification);
}
