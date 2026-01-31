package org.trung.tickethub.dto.ticket;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketRequest {
    String title;
    String description;
    double price;
    int maxPerPerson;
    int minPerPerson;
    int totalQuantity;
    boolean isActive;
    boolean isHidden;
    Long organiserId;
    Long eventId;
}
