package org.trung.tickethub.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trung.tickethub.dto.ticket.TicketRequest;
import org.trung.tickethub.dto.ticket.TicketResponse;
import org.trung.tickethub.entity.Event;
import org.trung.tickethub.entity.Organiser;
import org.trung.tickethub.entity.Ticket;
import org.trung.tickethub.exception.NotFoundException;
import org.trung.tickethub.mapper.TicketMapper;
import org.trung.tickethub.repository.EventRepository;
import org.trung.tickethub.repository.OrganiserRepository;
import org.trung.tickethub.repository.TicketRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TicketService {
    TicketRepository ticketRepository;
    EventRepository eventRepository;
    OrganiserRepository organiserRepository;
    TicketMapper ticketMapper;

    @Transactional
    public TicketResponse createTicket(TicketRequest request) {
        Ticket ticket = ticketMapper.toTicket(request);

        if (request.getOrganiserId() != null) {
            Organiser organiser = organiserRepository.findById(request.getOrganiserId())
                    .orElseThrow(() -> new NotFoundException("Organiser not found"));
            ticket.setOrganiser(organiser);
        }

        if (request.getEventId() != null) {
            Event event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> new NotFoundException("Event not found"));
            ticket.setEvent(event);
        }

        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Created ticket with id: {}", savedTicket.getId());
        return ticketMapper.toTicketResponse(savedTicket);
    }

    public TicketResponse getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));
        return ticketMapper.toTicketResponse(ticket);
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticketMapper::toTicketResponse)
                .toList();
    }

    public Page<TicketResponse> getTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable)
                .map(ticketMapper::toTicketResponse);
    }

    public List<TicketResponse> getTicketsByEventId(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException("Event not found");
        }
        return ticketRepository.findByEventId(eventId).stream()
                .map(ticketMapper::toTicketResponse)
                .toList();
    }

    @Transactional
    public TicketResponse updateTicket(Long id, TicketRequest request) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Ticket not found"));

        ticketMapper.updateTicket(request, ticket);

        if (request.getOrganiserId() != null) {
            Organiser organiser = organiserRepository.findById(request.getOrganiserId())
                    .orElseThrow(() -> new NotFoundException("Organiser not found"));
            ticket.setOrganiser(organiser);
        }

        if (request.getEventId() != null) {
            Event event = eventRepository.findById(request.getEventId())
                    .orElseThrow(() -> new NotFoundException("Event not found"));
            ticket.setEvent(event);
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        log.info("Updated ticket with id: {}", updatedTicket.getId());
        return ticketMapper.toTicketResponse(updatedTicket);
    }

    @Transactional
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new NotFoundException("Ticket not found");
        }
        ticketRepository.deleteById(id);
        log.info("Deleted ticket with id: {}", id);
    }
}
