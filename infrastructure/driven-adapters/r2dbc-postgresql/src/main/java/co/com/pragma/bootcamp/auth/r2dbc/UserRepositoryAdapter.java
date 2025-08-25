package co.com.pragma.bootcamp.auth.r2dbc;

import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import co.com.pragma.bootcamp.auth.r2dbc.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class UserRepositoryAdapter implements IUserRepository {

    private final IUserReactiveRepository userReactiveRepository;

    @Override
    public Mono<User> save(User user) {
        return userReactiveRepository.save(UserEntity.fromDomain(user))
                .map(UserEntity::toDomain);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return userReactiveRepository.findByEmail(email)
                .hasElement();
    }

    @Override
    public Flux<User> findAll() {
        return userReactiveRepository.findAll()
                .map(UserEntity::toDomain);
    }
}
