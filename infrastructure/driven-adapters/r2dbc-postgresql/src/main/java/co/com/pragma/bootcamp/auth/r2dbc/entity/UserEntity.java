package co.com.pragma.bootcamp.auth.r2dbc.entity;

import co.com.pragma.bootcamp.auth.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserEntity{
    @Id
    private Long id;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("birth_date")
    private LocalDate birthDate;
    private String address;
    private String phone;
    @Column("identification_number")
    private String identificationNumber;
    @Column("email")
    private String email;
    @Column("base_salary")
    private BigDecimal baseSalary;
    private String password;
    @Column("role_id")
    private Long roleId;

    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .birthDate(user.getBirthDate())
                .address(user.getAddress())
                .identificationNumber(user.getIdentificationNumber())
                .phone(user.getPhone())
                .email(user.getEmail())
                .baseSalary(user.getBaseSalary())
                .password(user.getPassword())
                .roleId(user.getRole().getId())
                .build();
    }


    public User toDomain() {
        return User.builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .birthDate(this.birthDate)
                .address(this.address)
                .identificationNumber(this.identificationNumber)
                .phone(this.phone)
                .email(this.email)
                .baseSalary(this.baseSalary)
                .password(this.password)
                .role(RoleEntity.builder().id(this.roleId).build().toDomain())
                .build();
    }
}
