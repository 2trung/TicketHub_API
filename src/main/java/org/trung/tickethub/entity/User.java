package org.trung.tickethub.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Id
    @GeneratedValue()
    private String id;
    String fullName;
    String username;
    String password;
    String phoneNumber;
    String gender;
    String email;
    String paymentId;
    String profilePictureUrl;
    Boolean isActive;
    String resetPasswordKey;
    Long resetPasswordExpiryTime;
    Long lastLoginTime;

    @CreationTimestamp
    Long createdAt;

    @UpdateTimestamp
    Long updatedAt;

    @ManyToMany
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles == null ? List.of() : List.copyOf(roles);
    }
}
