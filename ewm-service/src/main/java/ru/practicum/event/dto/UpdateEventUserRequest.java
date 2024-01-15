package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000)
    private String annotation;

    private Integer category;

    @Size(min = 20, max = 7000)
    private String description;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private String stateAction;
    @Size(min = 3, max = 120)
    private String title;

}
