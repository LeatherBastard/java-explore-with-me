package ru.practicum.participationrequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.*;
import ru.practicum.participationrequest.dto.ParticipationRequestDto;
import ru.practicum.participationrequest.mapper.ParticipationRequestMapper;
import ru.practicum.participationrequest.model.ParticipationRequest;
import ru.practicum.participationrequest.model.RequestStatus;
import ru.practicum.participationrequest.repository.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static ru.practicum.event.service.EventServiceImpl.EVENT_NOT_FOUND_MESSAGE;
import static ru.practicum.user.service.UserServiceImpl.USER_NOT_FOUND_MESSAGE;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
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
        if (optionalEvent.get().getInitiator().getId() == userId) {
            throw new ParticipationRequestOwnerParticipantException(eventId, userId);
        }

        List<ParticipationRequest> requests = repository.findByEvent_IdAndRequester_Id(eventId, userId);
        if (requests.size() > 0) {
            throw new ParticipationRequestAlreadyAddedException(eventId, userId);
        }

        if (!optionalEvent.get().getState().equals(EventState.PUBLISHED)) {
            throw new ParticipationRequestEventNotPublishedException(eventId);
        }
        if (optionalEvent.get().getConfirmedRequests().equals(optionalEvent.get().getParticipantLimit())) {
            throw new ParticipationRequestExceedLimitException(eventId, optionalEvent.get().getParticipantLimit());
        }

        ParticipationRequest participationRequest = ParticipationRequest.builder()
                .created(LocalDateTime.now())
                .event(optionalEvent.get())
                .requester(optionalUser.get())
                .build();

        if (optionalEvent.get().getRequestModeration()) {
            participationRequest.setStatus(RequestStatus.PENDING);
        } else {
            participationRequest.setStatus(RequestStatus.CONFIRMED);
        }
        return mapper.mapToParticipationRequestDto(repository.save(participationRequest));
    }
}
