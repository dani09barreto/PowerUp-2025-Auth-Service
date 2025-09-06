package co.com.pragma.bootcamp.auth.r2dbc.entity;

import co.com.pragma.bootcamp.auth.model.role.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("role")
public class RoleEntity {
    @Id
    private Long id;
    private String name;

    public static RoleEntity fromDomain(Role role) {
        return RoleEntity.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    public Role toDomain() {
        return Role.builder()
                .id(this.id)
                .name(this.name)
                .build();
    }
}
