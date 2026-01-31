package org.trung.tickethub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trung.tickethub.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
