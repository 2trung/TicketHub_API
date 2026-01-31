package org.trung.tickethub.dto.ticket;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketResponse {
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
    long createdAt;
    long updatedAt;
    Long organiserId;
    Long eventId;
}
