package co.com.pragma.bootcamp.auth.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserRegistrationRequest(
    String firstName,
    String lastName,
    LocalDate birthDate,
    String address,
    String phone,
    String identificationNumber,
    String email,
    BigDecimal baseSalary,
    String password,
    String roleName
) {
}
