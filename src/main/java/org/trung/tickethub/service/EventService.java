package org.trung.tickethub.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trung.tickethub.dto.event.EventRequest;
import org.trung.tickethub.dto.event.EventResponse;
import org.trung.tickethub.dto.event.SearchEventRequest;
import org.trung.tickethub.entity.Currency;
import org.trung.tickethub.entity.Event;
import org.trung.tickethub.entity.Organiser;
import org.trung.tickethub.exception.NotFoundException;
import org.trung.tickethub.mapper.EventMapper;
import org.trung.tickethub.repository.CurrencyRepository;
import org.trung.tickethub.repository.EventRepository;
import org.trung.tickethub.repository.OrganiserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventService {
    EventRepository eventRepository;
    OrganiserRepository organiserRepository;
    CurrencyRepository currencyRepository;
    EventMapper eventMapper;

    @Transactional
    public EventResponse createEvent(EventRequest request) {
        Event event = eventMapper.toEvent(request);

        if (request.getOrganiserId() != null) {
            Organiser organiser = organiserRepository.findById(request.getOrganiserId())
                    .orElseThrow(() -> new NotFoundException("Organiser not found"));
            event.setOrganiser(organiser);
        }

        if (request.getCurrencyId() != null) {
            Currency currency = currencyRepository.findById(request.getCurrencyId())
                    .orElseThrow(() -> new NotFoundException("Currency not found"));
            event.setCurrency(currency);
        }

        Event savedEvent = eventRepository.save(event);
        log.info("Created event with id: {}", savedEvent.getId());
        return eventMapper.toEventResponse(savedEvent);
    }

    public EventResponse getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));
        return eventMapper.toEventResponse(event);
    }

    public Page<EventResponse> getEvents(Pageable pageable) {
        return eventRepository.findAll(pageable)
                .map(eventMapper::toEventResponse);
    }

    @Transactional
    public EventResponse updateEvent(Long id, EventRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found"));

        eventMapper.updateEvent(request, event);

        if (request.getOrganiserId() != null) {
            Organiser organiser = organiserRepository.findById(request.getOrganiserId())
                    .orElseThrow(() -> new NotFoundException("Organiser not found"));
            event.setOrganiser(organiser);
        }

        if (request.getCurrencyId() != null) {
            Currency currency = currencyRepository.findById(request.getCurrencyId())
                    .orElseThrow(() -> new NotFoundException("Currency not found"));
            event.setCurrency(currency);
        }

        Event updatedEvent = eventRepository.save(event);
        log.info("Updated event with id: {}", updatedEvent.getId());
        return eventMapper.toEventResponse(updatedEvent);
    }

    @Transactional
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Event not found");
        }
        eventRepository.deleteById(id);
        log.info("Deleted event with id: {}", id);
    }


    public Page<EventResponse> searchEvents(SearchEventRequest request, Pageable pageable) {
        Long fromDate = request.getFromDate() != null ? Long.parseLong(request.getFromDate()) : null;
        Long toDate = request.getToDate() != null ? Long.parseLong(request.getToDate()) : null;
        return eventRepository.searchEvents(
                request.getKeyword(),
                fromDate,
                toDate,
                request.getLocation(),
                pageable
        ).map(eventMapper::toEventResponse);
    }
}
