package org.trung.tickethub.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.trung.tickethub.dto.SuccessResponse;
import org.trung.tickethub.dto.order.OrderRequest;
import org.trung.tickethub.dto.order.OrderResponse;
import org.trung.tickethub.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {
    OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return SuccessResponse.<OrderResponse>builder()
                .message("Order created successfully")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public SuccessResponse<OrderResponse> getOrder(@PathVariable String id) {
        OrderResponse response = orderService.getOrder(id);
        return SuccessResponse.<OrderResponse>builder()
                .data(response)
                .build();
    }

    @GetMapping
    public SuccessResponse<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> response = orderService.getAllOrders();
        return SuccessResponse.<List<OrderResponse>>builder()
                .data(response)
                .build();
    }

    @GetMapping("/page")
    public SuccessResponse<Page<OrderResponse>> getOrders(Pageable pageable) {
        Page<OrderResponse> response = orderService.getOrders(pageable);
        return SuccessResponse.<Page<OrderResponse>>builder()
                .data(response)
                .build();
    }

    @GetMapping("/user/{userId}")
    public SuccessResponse<List<OrderResponse>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderResponse> response = orderService.getOrdersByUserId(userId);
        return SuccessResponse.<List<OrderResponse>>builder()
                .data(response)
                .build();
    }

    @GetMapping("/event/{eventId}")
    public SuccessResponse<List<OrderResponse>> getOrdersByEventId(@PathVariable Long eventId) {
        List<OrderResponse> response = orderService.getOrdersByEventId(eventId);
        return SuccessResponse.<List<OrderResponse>>builder()
                .data(response)
                .build();
    }

    @PatchMapping("/{id}/status")
    public SuccessResponse<OrderResponse> updateOrderStatus(
            @PathVariable String id,
            @RequestParam String status) {
        OrderResponse response = orderService.updateOrderStatus(id, status);
        return SuccessResponse.<OrderResponse>builder()
                .message("Order status updated successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/{id}/pay")
    public SuccessResponse<OrderResponse> markOrderAsPaid(
            @PathVariable String id,
            @RequestParam String transactionId) {
        OrderResponse response = orderService.markOrderAsPaid(id, transactionId);
        return SuccessResponse.<OrderResponse>builder()
                .message("Order marked as paid")
                .data(response)
                .build();
    }

    @PatchMapping("/{id}/cancel")
    public SuccessResponse<OrderResponse> cancelOrder(@PathVariable String id) {
        OrderResponse response = orderService.cancelOrder(id);
        return SuccessResponse.<OrderResponse>builder()
                .message("Order cancelled successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
    }
}
