package ru.practicum.participationrequest.service;

import ru.practicum.participationrequest.dto.ParticipationRequestDto;

public interface ParticipationRequestService {
    ParticipationRequestDto addParticipationRequest(int userId, int eventId);
}
