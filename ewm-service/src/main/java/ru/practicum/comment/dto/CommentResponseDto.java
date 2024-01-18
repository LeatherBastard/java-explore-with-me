package ru.practicum.comment.dto;

import lombok.*;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.user.dto.UserShortDto;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class CommentResponseDto {
    private Integer id;
    private UserShortDto user;
    private EventShortDto event;
    private String text;
}
