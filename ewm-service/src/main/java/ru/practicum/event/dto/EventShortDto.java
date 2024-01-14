package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortDto {
    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    private String eventDate;

    private Integer id;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Integer views;
}
