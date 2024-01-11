package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.service.EventService;
import ru.practicum.participationrequest.dto.ParticipationRequestDto;
import ru.practicum.participationrequest.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateController {
    private static final String LOGGER_ADD_EVENT_MESSAGE = "Adding event";
    private static final String LOGGER_ADD_PARTICIPANT_REQUEST_MESSAGE = "Adding participant request";

    private static final String LOGGER_GET_EVENTS_MESSAGE = "Returning list of events for user with id: {}";

    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;


    @GetMapping("/{userId}/events")
    public List<EventFullDto> getEvents(@PathVariable("userId") int userId, @RequestParam(required = false, defaultValue = "0") int from, @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(LOGGER_GET_EVENTS_MESSAGE, userId);
        return eventService.getEvents(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable("userId") int userId, @RequestBody @Validated NewEventDto eventDto) {
        log.info(LOGGER_ADD_EVENT_MESSAGE);
        return eventService.addEvent(userId, eventDto);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable("userId") int userId, @RequestParam int eventId) {
        log.info(LOGGER_ADD_PARTICIPANT_REQUEST_MESSAGE);
        return participationRequestService.addParticipationRequest(userId, eventId);
    }

}
