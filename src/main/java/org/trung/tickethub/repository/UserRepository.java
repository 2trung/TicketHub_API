package org.trung.tickethub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.trung.tickethub.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}
