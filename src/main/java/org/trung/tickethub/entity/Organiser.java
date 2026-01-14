package org.trung.tickethub.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Organiser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String name;
    String about;
    String email;
    String phoneNumber;
    String facebookLink;
    String xLink;
    String instagramLink;
    String logoImagePath;

    @OneToMany
    Set<Event> events;
}
