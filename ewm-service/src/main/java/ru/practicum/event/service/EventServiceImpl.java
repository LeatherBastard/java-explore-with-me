package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.repository.EventRepository;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final Event
    @Override
    public EventFullDto addEvent(int userId, NewEventDto eventDto) {


        return null;
    }
}
