package org.trung.tickethub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trung.tickethub.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
}

