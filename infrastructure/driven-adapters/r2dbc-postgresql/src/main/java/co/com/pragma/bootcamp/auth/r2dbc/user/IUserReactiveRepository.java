package co.com.pragma.bootcamp.auth.r2dbc.user;

import co.com.pragma.bootcamp.auth.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IUserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>{

    @Query("SELECT * FROM users WHERE email = :email")
    Mono<UserEntity> findByEmail(String email);

    @Query("SELECT * FROM users WHERE identification_number = :numberIdentification")
    Mono<UserEntity> findByNumberIdentification(String numberIdentification);
}
