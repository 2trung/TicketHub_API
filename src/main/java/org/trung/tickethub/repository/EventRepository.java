package org.trung.tickethub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.trung.tickethub.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}

