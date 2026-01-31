package org.trung.tickethub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.trung.tickethub.dto.order.OrderItemResponse;
import org.trung.tickethub.dto.order.OrderResponse;
import org.trung.tickethub.entity.Order;
import org.trung.tickethub.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "orderItems", target = "items")
    OrderResponse toOrderResponse(Order order);

    @Mapping(source = "ticket.id", target = "ticketId")
    OrderItemResponse toOrderItemResponse(OrderItem orderItem);
}
