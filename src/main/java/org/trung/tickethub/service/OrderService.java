package org.trung.tickethub.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trung.tickethub.dto.order.OrderItemRequest;
import org.trung.tickethub.dto.order.OrderRequest;
import org.trung.tickethub.dto.order.OrderResponse;
import org.trung.tickethub.entity.*;
import org.trung.tickethub.exception.NotFoundException;
import org.trung.tickethub.mapper.OrderMapper;
import org.trung.tickethub.repository.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    EventRepository eventRepository;
    UserRepository userRepository;
    TicketRepository ticketRepository;
    OrderMapper orderMapper;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Order order = Order.builder()
                .note(request.getNote())
                .discountAmount(request.getDiscountAmount())
                .taxAmount(request.getTaxAmount())
                .status("PENDING")
                .isPaid(false)
                .isCancelled(false)
                .isRefunded(false)
                .isDeleted(false)
                .build();

        if (request.getUserId() != null) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new NotFoundException("User not found"));
            order.setUser(user);
        }

        if (request.getEventId() != null) {
            Event event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> new NotFoundException("Event not found"));
            order.setEvent(event);
        }

        Order savedOrder = orderRepository.save(order);

        // Process order items
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            Set<OrderItem> orderItems = new HashSet<>();
            long totalAmount = 0;

            for (OrderItemRequest itemRequest : request.getItems()) {
                OrderItem orderItem = OrderItem.builder()
                        .quantity(itemRequest.getQuantity())
                        .price(itemRequest.getPrice())
                        .order(savedOrder)
                        .build();

                if (itemRequest.getTicketId() != null) {
                    Ticket ticket = ticketRepository.findById(itemRequest.getTicketId())
                            .orElseThrow(() -> new NotFoundException("Ticket not found"));
                    orderItem.setTicket(ticket);

                    // Update sold quantity
                    ticket.setSoldQuantity(ticket.getSoldQuantity() + itemRequest.getQuantity());
                    ticketRepository.save(ticket);
                }

                OrderItem savedItem = orderItemRepository.save(orderItem);
                orderItems.add(savedItem);
                totalAmount += (long) (itemRequest.getPrice() * itemRequest.getQuantity());
            }

            savedOrder.setOrderItems(orderItems);
            savedOrder.setTotalAmount(totalAmount + request.getTaxAmount() - request.getDiscountAmount());
            savedOrder = orderRepository.save(savedOrder);
        }

        log.info("Created order with id: {}", savedOrder.getId());
        return orderMapper.toOrderResponse(savedOrder);
    }

    public OrderResponse getOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        return orderMapper.toOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    public Page<OrderResponse> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toOrderResponse);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    public List<OrderResponse> getOrdersByEventId(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found");
        }
        return orderRepository.findByEventId(eventId).stream()
                .map(orderMapper::toOrderResponse)
                .toList();
    }

    @Transactional
    public OrderResponse updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        log.info("Updated order status for id: {} to status: {}", id, status);
        return orderMapper.toOrderResponse(updatedOrder);
    }

    @Transactional
    public OrderResponse markOrderAsPaid(String id, String transactionId) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setPaid(true);
        order.setTransactionId(transactionId);
        order.setStatus("PAID");
        Order updatedOrder = orderRepository.save(order);
        log.info("Marked order as paid: {}", id);
        return orderMapper.toOrderResponse(updatedOrder);
    }

    @Transactional
    public OrderResponse cancelOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setCancelled(true);
        order.setStatus("CANCELLED");

        // Restore ticket quantities
        if (order.getOrderItems() != null) {
            for (OrderItem item : order.getOrderItems()) {
                if (item.getTicket() != null) {
                    Ticket ticket = item.getTicket();
                    ticket.setSoldQuantity(ticket.getSoldQuantity() - item.getQuantity());
                    ticketRepository.save(ticket);
                }
            }
        }

        Order updatedOrder = orderRepository.save(order);
        log.info("Cancelled order: {}", id);
        return orderMapper.toOrderResponse(updatedOrder);
    }

    @Transactional
    public void deleteOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setDeleted(true);
        orderRepository.save(order);
        log.info("Soft deleted order: {}", id);
    }
}
