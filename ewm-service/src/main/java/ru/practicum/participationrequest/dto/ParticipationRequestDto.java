package ru.practicum.participationrequest.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ParticipationRequestDto {
    private Integer id;
    private String created;
    private Integer event;
    private Integer requester;
    private String status;
}
