package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

public interface EventService {
    EventFullDto addEvent(int userId, NewEventDto eventDto);

    EventFullDto updateAdmin(int eventId, UpdateEventAdminRequest adminEventRequest);

}
