package org.trung.tickethub.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    Long eventId;
    Long userId;
    Set<OrderItemRequest> items;
    String note;
    long discountAmount;
    long taxAmount;
}
