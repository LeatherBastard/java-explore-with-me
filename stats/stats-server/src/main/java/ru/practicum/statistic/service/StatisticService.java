package ru.practicum.statistic.service;

import ru.practicum.statistic.dto.StatisticRequestDto;
import ru.practicum.statistic.dto.StatisticResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticService {
    void addHit(StatisticRequestDto statisticRequestDto);

    List<StatisticResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
