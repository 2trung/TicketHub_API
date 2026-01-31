package org.trung.tickethub.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.trung.tickethub.entity.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e WHERE " +
           "(:keyword IS NULL OR e.title LIKE %:keyword%) AND " +
           "(:fromDate IS NULL OR e.startDate >= :fromDate) AND " +
           "(:toDate IS NULL OR e.startDate <= :toDate) AND " +
           "(e.locationCity LIKE %:location%)")
    Page<Event> searchEvents(
            @Param("keyword") String keyword,
            @Param("fromDate") Long fromDate,
            @Param("toDate") Long toDate,
            @Param("location") String location,
            Pageable pageable);
}

