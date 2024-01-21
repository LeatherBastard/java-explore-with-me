package ru.practicum.location.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class LocationDto {
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
}
