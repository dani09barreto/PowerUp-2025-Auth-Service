package co.com.pragma.bootcamp.auth.api.mapper;


import co.com.pragma.bootcamp.auth.api.dto.UserRegistrationRequest;
import co.com.pragma.bootcamp.auth.api.dto.UserRegistrationResponse;
import co.com.pragma.bootcamp.auth.model.role.Role;
import co.com.pragma.bootcamp.auth.model.user.User;

public class UserDtoMapper {
    public static User toUser(UserRegistrationRequest request) {
        return User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .birthDate(request.birthDate())
                .address(request.address())
                .phone(request.phone())
                .email(request.email())
                .identificationNumber(request.identificationNumber())
                .baseSalary(request.baseSalary())
                .password(request.password())
                .role(Role.builder().name(request.roleName()).build())
                .build();
    }

    public static UserRegistrationResponse toUserRegistrationResponse(User user) {
        return new UserRegistrationResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getBirthDate(),
            user.getAddress(),
            user.getPhone(),
            user.getIdentificationNumber(),
            user.getEmail(),
            user.getBaseSalary()
        );
    }
}
