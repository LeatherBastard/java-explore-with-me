package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentResponseDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.participationrequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participationrequest.dto.EventRequestStatusUpdateResult;
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
    private static final String LOGGER_GET_EVENT_BY_ID_MESSAGE = "Getting event of user {} with id: {}";

    private static final String LOGGER_GET_USER_REQUESTS_MESSAGE = "Getting requests of user with id: {}";
    private static final String LOGGER_GET_REQUESTS_FOR_USER_EVENT_MESSAGE = "Returning list of user requests for user's {} event with id: {}";
    private static final String LOGGER_UPDATE_EVENT_MESSAGE = "Updating event of user {} with id: {}";

    private static final String LOGGER_CANCEL_REQUEST_MESSAGE = "Cancelling request with id {} for user {}";

    private static final String LOGGER_UPDATE_REQUESTS_MESSAGE = "Updating requests status for user`s {} event with id: {}";
    private static final String LOGGER_GET_EVENTS_MESSAGE = "Returning list of events for user with id: {}";

    private final EventService eventService;
    private final CommentService commentService;
    private final ParticipationRequestService participationRequestService;


    @GetMapping("/{userId}/events")
    public List<EventFullDto> getEvents(@PathVariable("userId") int userId, @RequestParam(required = false, defaultValue = "0") int from, @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(LOGGER_GET_EVENTS_MESSAGE, userId);
        return eventService.findAllEventsByUser(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable("userId") int userId, @PathVariable("eventId") int eventId) {
        log.info(LOGGER_GET_EVENT_BY_ID_MESSAGE, userId, eventId);
        return eventService.findUserEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateUserEvent(@PathVariable("userId") int userId, @PathVariable("eventId") int eventId, @RequestBody @Validated UpdateEventUserRequest userEventRequest) {
        log.info(LOGGER_UPDATE_EVENT_MESSAGE, userId, eventId);
        return eventService.updateEventByUser(userId, eventId, userEventRequest);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable("userId") int userId, @RequestBody @Validated NewEventDto eventDto) {
        log.info(LOGGER_ADD_EVENT_MESSAGE);
        return eventService.addEvent(userId, eventDto);
    }


    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getUserParticipationRequests(@PathVariable("userId") int userId) {
        log.info(LOGGER_GET_USER_REQUESTS_MESSAGE, userId);
        return participationRequestService.findUserParticipationRequests(userId);

    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto addParticipationRequest(@PathVariable("userId") int userId, @RequestParam int eventId) {
        log.info(LOGGER_ADD_PARTICIPANT_REQUEST_MESSAGE);
        return participationRequestService.addParticipationRequest(userId, eventId);
    }

    @PatchMapping("{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelParticipationRequest(@PathVariable("userId") int userId, @PathVariable int requestId) {
        log.info(LOGGER_CANCEL_REQUEST_MESSAGE, requestId, userId);
        return participationRequestService.cancelParticipationRequest(userId, requestId);
    }


    @GetMapping("/{userId}/events/{eventId}/requests")
    List<ParticipationRequestDto> getUserEventParticipationRequests(@PathVariable("userId") int userId, @PathVariable("eventId") int eventId) {
        log.info(LOGGER_GET_REQUESTS_FOR_USER_EVENT_MESSAGE, userId, eventId);
        return participationRequestService.findUserEventParticipationRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    EventRequestStatusUpdateResult updateEventRequests(@PathVariable("userId") int userId, @PathVariable("eventId") int eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info(LOGGER_UPDATE_REQUESTS_MESSAGE, userId, eventId);
        return participationRequestService
                .updateUserEventParticipationRequests(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @PostMapping("/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(@PathVariable("userId") int userId, @PathVariable("eventId") int eventId, @RequestBody @Validated NewCommentDto commentDto) {
        log.info(LOGGER_ADD_EVENT_MESSAGE);
        return commentService.addComment(userId, eventId, commentDto);
    }

    @GetMapping("/{userId}/comments")
    public List<CommentResponseDto> getComments(@PathVariable("userId") int userId, @RequestParam(required = false, defaultValue = "0") int from, @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(LOGGER_GET_COMMENTS_MESSAGE, userId);
        return commentService.findAllCommentsByUser(userId, from, size);
    }


}
