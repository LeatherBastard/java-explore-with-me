package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto addEvent(int userId, NewEventDto eventDto);

    List<EventFullDto> getEvents(int userId, int from, int size);

    List<EventFullDto> findAllEvents(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateAdmin(int eventId, UpdateEventAdminRequest adminEventRequest);

}
