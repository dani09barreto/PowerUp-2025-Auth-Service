package co.com.pragma.bootcamp.auth.usecase.registrationuser;

import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegistrationUserUseCase implements IRegistrationUserUseCase{

    private final IUserRepository userRepository;

    @Override
    public Mono<User> registerUser(User user) {

        // Validate user fields
        validateUser(user);

        return userRepository.existsByEmail(user.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new IllegalArgumentException("Email already exists"));
                    }
                    return userRepository.save(user);
                });
    }

    @Override
    public void validateUser(User user) throws IllegalArgumentException {
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (user.getBirthDate() == null) {
            throw new IllegalArgumentException("Birth date cannot be null");
        }
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be empty");
        }
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            throw new IllegalArgumentException("Phone cannot be empty");
        }
        if (user.getIdentificationNumber() == null || user.getIdentificationNumber().isEmpty()) {
            throw new IllegalArgumentException("Identification number cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (user.getBaseSalary() == null || user.getBaseSalary().doubleValue() < 0 || user.getBaseSalary().doubleValue() > 15000000) {
            throw new IllegalArgumentException("Base salary must be between 0 and 15,000,000");
        }
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new IllegalArgumentException("Invalid email format");
    }
}
