package org.trung.tickethub.repository;

import org.springframework.stereotype.Repository;
import org.trung.tickethub.entity.Attendee;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AttendeeRepository extends JpaRepository<Attendee, Long> {
}


