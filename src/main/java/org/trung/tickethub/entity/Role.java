package org.trung.tickethub.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role implements GrantedAuthority {
    @Id
    String name;

    String description;

    @Override
    public String getAuthority() {
        return name;
    }
}
