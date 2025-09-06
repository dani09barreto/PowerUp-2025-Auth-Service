package co.com.pragma.bootcamp.auth.r2dbc.role;

import co.com.pragma.bootcamp.auth.r2dbc.entity.RoleEntity;
import co.com.pragma.bootcamp.auth.r2dbc.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IRoleReactiveRepository extends ReactiveCrudRepository<RoleEntity, Long>{

    @Query("SELECT * FROM role WHERE name = :name")
    Mono<RoleEntity> findByName(String name);
}
