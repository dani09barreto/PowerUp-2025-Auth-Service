package co.com.pragma.bootcamp.auth.model.user.gateways;

import co.com.pragma.bootcamp.auth.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUserRepository {
    Mono<User> save(User user);
    Mono<Boolean> existsByEmail(String email);
    Flux<User> findAll();
    Mono<User> findByNumberIdentification(String numberIdentification);
}
