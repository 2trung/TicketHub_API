package org.trung.tickethub.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Set;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    long id;

    String symbolLeft;
    String symbolRight;
    String code;
    String decimalPlaces;
    String decimalPoint;
    String thousandsPoint;

    double value;

    @CreationTimestamp
    long createdAt;

    @UpdateTimestamp
    long updatedAt;

    @OneToMany
    Set<Event> events;

}
