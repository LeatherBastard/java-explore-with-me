package ru.practicum.participationrequest.service;

import ru.practicum.participationrequest.dto.EventRequestStatusUpdateRequest;
import ru.practicum.participationrequest.dto.EventRequestStatusUpdateResult;
import ru.practicum.participationrequest.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {
    ParticipationRequestDto addParticipationRequest(int userId, int eventId);

    ParticipationRequestDto cancelParticipationRequest(int userId, int requestId);

    List<ParticipationRequestDto> findUserParticipationRequests(int userId);

    List<ParticipationRequestDto> findUserEventParticipationRequests(int userId, int eventId);

    EventRequestStatusUpdateResult updateUserEventParticipationRequests(int userId, int eventId, EventRequestStatusUpdateRequest requestStatusUpdateRequest);
}
