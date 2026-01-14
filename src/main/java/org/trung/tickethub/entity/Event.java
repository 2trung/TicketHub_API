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
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String title;
    String bgColor;
    String bgImagePath;
    String description;
    long startDate;
    long endDate;
    int salesVolume;
    String locationName;
    String locationAddressLine1;
    String locationAddressLine2;
    String locationCity;
    String locationState;
    String locationLatitude;
    String locationLongitude;
    boolean isOnlineEvent;

    @CreationTimestamp
    long createdAt;

    @UpdateTimestamp
    long updatedAt;

    @ManyToOne
    Organiser organiser;

    @ManyToOne
    Currency currency;
}
