package ru.practicum.event.dto;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class NewEventDto {
    @NotNull
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    @NotNull
    private Boolean paid;
    @NotNull
    private Integer participantLimit;
    @NotNull
    private Boolean requestModeration;
    @NotNull
    @NotBlank
    private String title;
    @NotNull
    private Integer views;
}
