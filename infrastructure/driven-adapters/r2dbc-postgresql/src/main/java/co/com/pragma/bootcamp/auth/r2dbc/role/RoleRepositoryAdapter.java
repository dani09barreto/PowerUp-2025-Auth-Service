package co.com.pragma.bootcamp.auth.r2dbc.role;

import co.com.pragma.bootcamp.auth.model.role.Role;
import co.com.pragma.bootcamp.auth.model.role.gateways.IRoleRepository;
import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import co.com.pragma.bootcamp.auth.r2dbc.entity.RoleEntity;
import co.com.pragma.bootcamp.auth.r2dbc.entity.UserEntity;
import co.com.pragma.bootcamp.auth.r2dbc.user.IUserReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@Repository
public class RoleRepositoryAdapter implements IRoleRepository {

    private final IRoleReactiveRepository roleReactiveRepository;

    @Override
    public Mono<Role> getRoleByName(String name) {
        return roleReactiveRepository.findByName(name)
                .doOnNext(role -> log.info("Found role by name: {}", role))
                .map(RoleEntity::toDomain);
    }

    @Override
    public Mono<Role> getRoleById(Long id) {
        return roleReactiveRepository.findById(id)
                .doOnNext(role -> log.info("Found role by id: {}", role))
                .map(RoleEntity::toDomain);
    }
}
