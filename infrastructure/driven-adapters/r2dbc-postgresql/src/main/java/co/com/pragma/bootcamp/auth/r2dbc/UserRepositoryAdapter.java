package co.com.pragma.bootcamp.auth.r2dbc;

import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import co.com.pragma.bootcamp.auth.r2dbc.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@Repository
public class UserRepositoryAdapter implements IUserRepository {

    private final IUserReactiveRepository userReactiveRepository;

    @Transactional
    @Override
    public Mono<User> save(User user) {
        log.info("Saving user: {}", user);
        return userReactiveRepository.save(UserEntity.fromDomain(user))
                .map(UserEntity::toDomain);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        log.info("Checking existence of user with email: {}", email);
        return userReactiveRepository.findByEmail(email)
                .doOnNext(userEntity -> log.info("User by email"))
                .hasElement();
    }

    @Override
    public Flux<User> findAll() {
        log.info("Retrieving all users");
        return userReactiveRepository.findAll()
                .map(UserEntity::toDomain);
    }
}
