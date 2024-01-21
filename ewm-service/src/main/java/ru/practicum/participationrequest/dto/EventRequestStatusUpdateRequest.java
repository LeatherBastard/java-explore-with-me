package ru.practicum.participationrequest.dto;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private String status;
}
