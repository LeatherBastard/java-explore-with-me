package ru.practicum.event.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;

@Component
public class EventMapper {
    public EventFullDto mapToEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();

    }

    public Event mapToEvent(NewEventDto eventDto) {
        return E
    }
}
