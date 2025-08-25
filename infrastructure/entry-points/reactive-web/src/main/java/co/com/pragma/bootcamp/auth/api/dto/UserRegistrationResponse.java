package co.com.pragma.bootcamp.auth.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserRegistrationResponse(
    Long id,
    String firstName,
    String lastName,
    LocalDate birthDate,
    String address,
    String phone,
    String email,
    BigDecimal baseSalary
) {
}
