package ru.practicum.statistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class StatisticResponseDto {
    private String app;
    private String uri;
    private Integer hits;
}
