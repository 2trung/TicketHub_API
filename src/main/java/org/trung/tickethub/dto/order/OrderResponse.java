package org.trung.tickethub.dto.order;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderResponse {
    String id;
    String status;
    String ticketPdfPath;
    String transactionId;
    long discountAmount;
    long totalAmount;
    long refundAmount;
    long taxAmount;
    boolean isPaid;
    boolean isCancelled;
    boolean isRefunded;
    boolean isDeleted;
    String note;
    long createdAt;
    long updatedAt;
    Long userId;
    Long eventId;
    Set<OrderItemResponse> items;
}
