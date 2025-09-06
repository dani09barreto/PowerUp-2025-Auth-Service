package co.com.pragma.bootcamp.auth.usecase.registrationuser;

import co.com.pragma.bootcamp.auth.model.role.gateways.IRoleRepository;
import co.com.pragma.bootcamp.auth.model.token.gateways.TokenRepository;
import co.com.pragma.bootcamp.auth.model.user.User;
import co.com.pragma.bootcamp.auth.model.user.gateways.IUserRepository;
import co.com.pragma.bootcamp.auth.usecase.error.InvalidUserDataException;
import co.com.pragma.bootcamp.auth.usecase.error.RoleNotFoundException;
import co.com.pragma.bootcamp.auth.usecase.error.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegistrationUserUseCase implements IRegistrationUserUseCase{

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final TokenRepository tokenRepository;

    @Override
    public Mono<User> registerUser(User user) {

        // Validate user fields
        validateUser(user);

        return roleRepository.getRoleByName(user.getRole().getName())
                .switchIfEmpty(Mono.error(new RoleNotFoundException("Role not found: " + user.getRole().getName())))
                .flatMap(role -> userRepository.existsByEmail(user.getEmail())
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(
                                        new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists")
                                );
                            }
                            user.setRole(role);
                            user.setPassword(tokenRepository.encodePassword(user.getPassword()));
                            return userRepository.save(user);
                        }));
    }

    @Override
    public void validateUser(User user) throws IllegalArgumentException {
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            throw new InvalidUserDataException("First name cannot be null");
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            throw new InvalidUserDataException("Last name cannot be empty");
        }
        if (user.getBirthDate() == null) {
            throw new InvalidUserDataException("Birth date cannot be null");
        }
        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            throw new InvalidUserDataException("Address cannot be empty");
        }
        if (user.getPhone() == null || user.getPhone().isEmpty()) {
            throw new InvalidUserDataException("Phone cannot be empty");
        }
        if (user.getIdentificationNumber() == null || user.getIdentificationNumber().isEmpty()) {
            throw new InvalidUserDataException("Identification number cannot be empty");
        }
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new InvalidUserDataException("Email cannot be empty");
        }
        if (user.getBaseSalary() == null || user.getBaseSalary().doubleValue() < 0 || user.getBaseSalary().doubleValue() > 15000000) {
            throw new InvalidUserDataException("Base salary must be between 0 and 15,000,000");
        }
        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$"))
            throw new InvalidUserDataException("Invalid email format");

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new InvalidUserDataException("Password cannot be empty");
        }
        if (user.getRole() == null) {
            throw new InvalidUserDataException("Role cannot be null");
        }
    }
}
