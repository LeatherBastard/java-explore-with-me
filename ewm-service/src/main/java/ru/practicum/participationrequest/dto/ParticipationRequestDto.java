package ru.practicum.participationrequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {
    private Integer id;
    private String created;
    private Integer event;
    private Integer requester;
    private String status;
}
