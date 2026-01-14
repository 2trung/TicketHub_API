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
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @CreationTimestamp
    long createdAt;

    @UpdateTimestamp
    long updatedAt;


    @ManyToOne
    User user;

    @ManyToOne
    Event event;

    @OneToMany
    Set<OrderItem> orderItems;

    @OneToMany
    Set<Ticket> tickets;
}
