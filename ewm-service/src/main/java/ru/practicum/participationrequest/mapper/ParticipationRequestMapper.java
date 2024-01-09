package ru.practicum.participationrequest.mapper;

import ru.practicum.participationrequest.dto.ParticipationRequestDto;
import ru.practicum.participationrequest.model.ParticipationRequest;

import java.time.format.DateTimeFormatter;

public class ParticipationRequestMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ParticipationRequestDto mapToParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .created(participationRequest.getCreated().format(formatter))
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus().name())
                .build();
    }


}
