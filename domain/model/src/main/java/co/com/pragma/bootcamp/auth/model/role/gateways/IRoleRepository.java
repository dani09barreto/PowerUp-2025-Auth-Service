package co.com.pragma.bootcamp.auth.model.role.gateways;

import co.com.pragma.bootcamp.auth.model.role.Role;
import reactor.core.publisher.Mono;

public interface IRoleRepository {
    Mono<Role> getRoleByName(String name);
    Mono<Role> getRoleById(Long id);
}
