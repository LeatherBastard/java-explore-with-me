package ru.practicum.event.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

@Component
public class EventMapper {
    public EventFullDto mapToEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(
                        CategoryDto.builder()
                                .id(event.getCategory().getId())
                                .name(event.getCategory().getName())
                                .build()
                )
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(
                        UserShortDto.builder()
                                .id(event.getInitiator().getId())
                                .name(event.getInitiator().getName())
                                .build()
                )
                .location(LocationDto.builder()
                        .latitude(event.getLocation().getLatitude())
                        .longitude(event.getLocation().getLongitude())
                        .build())
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
        return Event.builder()
                .annotation(eventDto.getAnnotation())
                .description(eventDto.getDescription())
                .eventDate(eventDto.getEventDate())
                .paid(eventDto.getPaid())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.getRequestModeration())
                .title(eventDto.getTitle())
                .views(eventDto.getViews())
                .build();
    }
}
