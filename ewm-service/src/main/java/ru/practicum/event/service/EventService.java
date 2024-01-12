package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto addEvent(int userId, NewEventDto eventDto);

    EventFullDto findUserEventById(int userId, int eventId);

    List<EventFullDto> findAllUserEvents(int userId, int from, int size);

    List<EventFullDto> findAllEventsByAdmin(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest userEventRequest);

    EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest adminEventRequest);

}