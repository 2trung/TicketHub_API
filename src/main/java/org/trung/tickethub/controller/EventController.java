package org.trung.tickethub.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.trung.tickethub.dto.SuccessResponse;
import org.trung.tickethub.dto.event.EventRequest;
import org.trung.tickethub.dto.event.EventResponse;
import org.trung.tickethub.dto.event.SearchEventRequest;
import org.trung.tickethub.service.EventService;


@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EventController {
    EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponse<EventResponse> createEvent(@RequestBody EventRequest request) {
        EventResponse response = eventService.createEvent(request);
        return SuccessResponse.<EventResponse>builder()
                .message("Event created successfully")
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public SuccessResponse<EventResponse> getEvent(@PathVariable Long id) {
        EventResponse response = eventService.getEvent(id);
        return SuccessResponse.<EventResponse>builder()
                .data(response)
                .build();
    }

    @GetMapping
    public SuccessResponse<Page<EventResponse>> getEvents(Pageable pageable) {
        Page<EventResponse> response = eventService.getEvents(pageable);
        return SuccessResponse.<Page<EventResponse>>builder()
                .data(response)
                .build();
    }

    @PutMapping("/{id}")
    public SuccessResponse<EventResponse> updateEvent(@PathVariable Long id, @RequestBody EventRequest request) {
        EventResponse response = eventService.updateEvent(id, request);
        return SuccessResponse.<EventResponse>builder()
                .message("Event updated successfully")
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }


    @GetMapping("/search")
    public SuccessResponse<Page<EventResponse>> searchEvents(SearchEventRequest request, Pageable pageable) {
        Page<EventResponse> response = eventService.searchEvents(request, pageable);
        return SuccessResponse.<Page<EventResponse>>builder()
                .data(response)
                .build();
    }
}
