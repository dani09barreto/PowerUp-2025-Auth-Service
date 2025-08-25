package co.com.pragma.bootcamp.auth.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserRegistrationRequest(
    String firstName,
    String lastName,
    LocalDate birthDate,
    String address,
    String phone,
    String email,
    BigDecimal baseSalary
) {
}
