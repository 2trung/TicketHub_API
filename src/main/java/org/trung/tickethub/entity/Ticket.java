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
public class Ticket {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    long id;

    String title;
    String description;
    double price;
    int maxPerPerson;
    int minPerPerson;
    int totalQuantity;
    int soldQuantity;
    boolean isActive;
    boolean isHidden;

    @CreationTimestamp
    long createdAt;

    @UpdateTimestamp
    long updatedAt;

    @ManyToOne
    Organiser organiser;

}
