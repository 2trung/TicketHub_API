package org.trung.tickethub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trung.tickethub.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long> {
}

