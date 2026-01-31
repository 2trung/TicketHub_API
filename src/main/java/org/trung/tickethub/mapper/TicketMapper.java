package org.trung.tickethub.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.trung.tickethub.dto.ticket.TicketRequest;
import org.trung.tickethub.dto.ticket.TicketResponse;
import org.trung.tickethub.entity.Ticket;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "soldQuantity", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "organiser", ignore = true)
    @Mapping(target = "event", ignore = true)
    Ticket toTicket(TicketRequest request);

    @Mapping(source = "organiser.id", target = "organiserId")
    @Mapping(source = "event.id", target = "eventId")
    TicketResponse toTicketResponse(Ticket ticket);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "soldQuantity", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "organiser", ignore = true)
    @Mapping(target = "event", ignore = true)
    void updateTicket(TicketRequest request, @MappingTarget Ticket ticket);
}
