package org.trung.tickethub.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Attendee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String firstName;
    String lastName;
    String email;

    @CreationTimestamp
    long createdAt;

    @UpdateTimestamp
    long updatedAt;

    long arrivalTime;

    @ManyToOne
    User user;

    @ManyToOne
    Event event;
}
