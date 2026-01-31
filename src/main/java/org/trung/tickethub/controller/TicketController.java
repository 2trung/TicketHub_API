package org.trung.tickethub.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.trung.tickethub.dto.SuccessResponse;
import org.trung.tickethub.dto.ticket.TicketRequest;
import org.trung.tickethub.dto.ticket.TicketResponse;
import org.trung.tickethub.service.TicketService;

import java.util.List;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketController {
    TicketService ticketService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<TicketResponse> createTicket(@RequestBody TicketRequest request) {
        TicketResponse response = ticketService.createTicket(request);
        return SuccessResponse.<TicketResponse>builder()
                .message("Ticket created successfully")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public SuccessResponse<TicketResponse> getTicket(@PathVariable Long id) {
        TicketResponse response = ticketService.getTicket(id);
        return SuccessResponse.<TicketResponse>builder()
                .data(response)
                .build();
    }

    @GetMapping
    public SuccessResponse<List<TicketResponse>> getAllTickets() {
        List<TicketResponse> response = ticketService.getAllTickets();
        return SuccessResponse.<List<TicketResponse>>builder()
                .data(response)
                .build();
    }

    @GetMapping("/page")
    public SuccessResponse<Page<TicketResponse>> getTickets(Pageable pageable) {
        Page<TicketResponse> response = ticketService.getTickets(pageable);
        return SuccessResponse.<Page<TicketResponse>>builder()
                .data(response)
                .build();
    }

    @GetMapping("/event/{eventId}")
    public SuccessResponse<List<TicketResponse>> getTicketsByEventId(@PathVariable Long eventId) {
        List<TicketResponse> response = ticketService.getTicketsByEventId(eventId);
        return SuccessResponse.<List<TicketResponse>>builder()
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    public SuccessResponse<TicketResponse> updateTicket(@PathVariable Long id, @RequestBody TicketRequest request) {
        TicketResponse response = ticketService.updateTicket(id, request);
        return SuccessResponse.<TicketResponse>builder()
                .message("Ticket updated successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }
}
