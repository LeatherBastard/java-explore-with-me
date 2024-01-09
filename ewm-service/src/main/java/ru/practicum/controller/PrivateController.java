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

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateController {
    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable("userId") int userId, @RequestBody @Validated NewEventDto eventDto) {
        return eventService.addEvent(userId, eventDto);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable("userId") int userId, @RequestParam int eventId) {
        return participationRequestService.addParticipationRequest(userId, eventId);
    }

}
