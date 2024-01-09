package ru.practicum.participationrequest.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.participationrequest.dto.ParticipationRequestDto;
import ru.practicum.participationrequest.model.ParticipationRequest;

import java.time.format.DateTimeFormatter;

@Component
public class ParticipationRequestMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ParticipationRequestDto mapToParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .created(participationRequest.getCreated().format(formatter))
                .event(participationRequest.getEvent().getId())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getStatus().name())
                .build();
    }


}
