package ru.practicum.statistic.service;

import ru.practicum.statistic.dto.StatisticRequestDto;
import ru.practicum.statistic.dto.StatisticResponseDto;

import java.util.List;

public interface StatisticService {
    void addHit(StatisticRequestDto statisticRequestDto);

    List<StatisticResponseDto> getStats(String start, String end, List<String> uris, boolean unique);
}
