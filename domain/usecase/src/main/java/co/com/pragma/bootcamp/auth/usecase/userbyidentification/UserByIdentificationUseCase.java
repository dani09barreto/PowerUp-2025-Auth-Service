package co.com.pragma.bootcamp.auth.usecase.userbyidentification;

import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import co.com.pragma.bootcamp.auth.usecase.error.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserByIdentificationUseCase implements IUserByIdentificationUseCase {
    private final IUserRepository userRepository;

    @Override
    public Mono<User> getUserByIdentification(String numberIdentification) {
        return userRepository.findByNumberIdentification(numberIdentification)
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with identification number: " + numberIdentification)));
    }
}
