package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.service.EventService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateController {
    private final EventService eventService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable("userId") int userId, @RequestBody NewEventDto eventDto) {
        return eventService.addEvent(userId, eventDto);
    }

}
