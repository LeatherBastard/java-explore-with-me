package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventAdminRequest;
import ru.practicum.event.dto.UpdateEventUserRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto addEvent(int userId, NewEventDto eventDto);

    EventFullDto getById(int id, HttpServletRequest request);

    EventFullDto findUserEventById(int userId, int eventId);

    List<EventFullDto> findAllEvents(String text,
                                     List<Integer> categories,
                                     Boolean paid,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Boolean onlyAvailable,
                                     String sort,
                                     int from,
                                     int size);

    List<EventFullDto> findAllEventsByUser(int userId, int from, int size);

    List<EventFullDto> findAllEventsByAdmin(List<Integer> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventByUser(int userId, int eventId, UpdateEventUserRequest userEventRequest);

    EventFullDto updateEventByAdmin(int eventId, UpdateEventAdminRequest adminEventRequest);

}
