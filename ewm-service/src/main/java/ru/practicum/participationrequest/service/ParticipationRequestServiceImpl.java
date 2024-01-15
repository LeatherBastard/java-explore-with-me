package ru.practicum.participationrequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.*;
import ru.practicum.participationrequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participationrequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.participationrequest.dto.ParticipationRequestDto;
import ru.practicum.participationrequest.mapper.ParticipationRequestMapper;
import ru.practicum.participationrequest.model.ParticipationRequest;
import ru.practicum.participationrequest.model.RequestStatus;
import ru.practicum.participationrequest.repository.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.event.service.EventServiceImpl.EVENT_NOT_FOUND_MESSAGE;
import static ru.practicum.user.service.UserServiceImpl.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    public static final String PARTICIPATION_REQUEST_NOT_FOUND_MESSAGE = "Participation request with id %d not found";

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository repository;
    private final ParticipationRequestMapper mapper;

    @Override
    public ParticipationRequestDto addParticipationRequest(int userId, int eventId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException(EVENT_NOT_FOUND_MESSAGE, eventId);

        Event event = optionalEvent.get();
        if (event.getInitiator().getId() == userId) {
            throw new ParticipationRequestOwnerParticipantException(eventId, userId);
        }

        List<ParticipationRequest> requests = repository.findByEvent_IdAndRequester_Id(eventId, userId);
        if (!requests.isEmpty()) {
            throw new ParticipationRequestAlreadyAddedException(eventId, userId);
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ParticipationRequestEventNotPublishedException(eventId);
        }


        if (event.getParticipantLimit() > 0
                && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ParticipationRequestExceedLimitException(eventId, event.getParticipantLimit());
        }

        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(event)
                .requester(optionalUser.get())
                .build();
        boolean confirmedRequest = false;
        if (event.getRequestModeration()) {
            if (event.getParticipantLimit() > 0)
                participationRequest.setStatus(RequestStatus.PENDING);
            else {
                participationRequest.setStatus(RequestStatus.CONFIRMED);
                confirmedRequest = true;
            }
        } else {
            participationRequest.setStatus(RequestStatus.CONFIRMED);
            confirmedRequest = true;
        }

        if (confirmedRequest) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        return mapper.mapToParticipationRequestDto(repository.save(participationRequest));
    }

    @Override
    public ParticipationRequestDto cancelParticipationRequest(int userId, int requestId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<ParticipationRequest> optionalParticipationRequest = repository.findById(requestId);
        if (optionalParticipationRequest.isEmpty())
            throw new EntityNotFoundException(PARTICIPATION_REQUEST_NOT_FOUND_MESSAGE, requestId);
        int eventId = optionalParticipationRequest.get().getEvent().getId();
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException(EVENT_NOT_FOUND_MESSAGE, eventId);
        Event event = optionalEvent.get();
        ParticipationRequest request = optionalParticipationRequest.get();
        request.setStatus(RequestStatus.CANCELED);
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.save(event);
        return mapper.mapToParticipationRequestDto(repository.save(request));
    }

    @Override
    public List<ParticipationRequestDto> findUserParticipationRequests(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        return repository.findByRequester_Id(userId).stream().map(mapper::mapToParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> findUserEventParticipationRequests(int userId, int eventId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException(EVENT_NOT_FOUND_MESSAGE, eventId);
        if (optionalEvent.get().getInitiator().getId() != userId) {
            throw new ParticipationRequestOwnerParticipantException(eventId, userId);
        }
        return repository.findByEvent_Id(eventId).stream().map(mapper::mapToParticipationRequestDto).collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateUserEventParticipationRequests(int userId, int eventId, EventRequestStatusUpdateRequest requestStatusUpdateRequest) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty())
            throw new EntityNotFoundException(USER_NOT_FOUND_MESSAGE, userId);
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty())
            throw new EntityNotFoundException(EVENT_NOT_FOUND_MESSAGE, eventId);
        if (optionalEvent.get().getInitiator().getId() != userId) {
            throw new ParticipationRequestOwnerParticipantException(eventId, userId);
        }
        Event event = optionalEvent.get();
        List<ParticipationRequest> requests = new ArrayList<>();
        for (Integer requestId : requestStatusUpdateRequest.getRequestIds()) {
            Optional<ParticipationRequest> request = repository.findById(requestId);
            if (request.isEmpty())
                throw new EntityNotFoundException(PARTICIPATION_REQUEST_NOT_FOUND_MESSAGE, requestId);
            if (!request.get().getEvent().getId().equals(eventId))
                throw new ParticipationRequestWrongIdException(request.get().getEvent().getId(), eventId);
            requests.add(request.get());
        }

        EventRequestStatusUpdateResult result = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();


        if (event.getConfirmedRequests().equals(event.getParticipantLimit()) && event.getParticipantLimit() > 0) {
            throw new ParticipationRequestExceedLimitException(eventId, event.getParticipantLimit());
        }
        int confirmedRequests = 0;
        for (ParticipationRequest request : requests) {
            if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
                throw new ParticipationRequestExceedLimitException(eventId, event.getParticipantLimit());
            } else {
                request.setStatus(RequestStatus.valueOf(requestStatusUpdateRequest.getStatus()));
                if (request.getStatus().equals(RequestStatus.CONFIRMED)) {
                    result.getConfirmedRequests()
                            .add(
                                    mapper.mapToParticipationRequestDto(repository.save(request))
                            );
                    confirmedRequests++;
                } else {
                    result.getRejectedRequests()
                            .add(
                                    mapper.mapToParticipationRequestDto(repository.save(request))
                            );
                }
            }
        }
        if (confirmedRequests > 0) {
            event.setConfirmedRequests(event.getConfirmedRequests() + confirmedRequests);
            eventRepository.save(event);
        }


        return result;
    }
}
