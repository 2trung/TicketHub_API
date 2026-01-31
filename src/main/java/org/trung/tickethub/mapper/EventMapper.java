package org.trung.tickethub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.trung.tickethub.dto.event.EventRequest;
import org.trung.tickethub.dto.event.EventResponse;
import org.trung.tickethub.entity.Event;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salesVolume", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "organiser", ignore = true)
    @Mapping(target = "currency", ignore = true)
    Event toEvent(EventRequest request);

    @Mapping(source = "organiser.id", target = "organiserId")
    @Mapping(source = "currency.id", target = "currencyId")
    EventResponse toEventResponse(Event event);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "salesVolume", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "organiser", ignore = true)
    @Mapping(target = "currency", ignore = true)
    void updateEvent(EventRequest request, @MappingTarget Event event);
}
