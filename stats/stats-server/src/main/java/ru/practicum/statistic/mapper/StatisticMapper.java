package ru.practicum.statistic.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.statistic.dto.StatisticRequestDto;
import ru.practicum.statistic.dto.StatisticResponseDto;
import ru.practicum.statistic.model.Statistic;
import ru.practicum.statistic.model.view.StatisticView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatisticMapper {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public Statistic mapToStatistic(StatisticRequestDto statisticRequestDto) {
        return Statistic.builder()
                .app(statisticRequestDto.getApp())
                .uri(statisticRequestDto.getUri())
                .ip(statisticRequestDto.getIp())
                .timestamp(LocalDateTime.parse(statisticRequestDto.getTimestamp(), formatter))
                .build();
    }

    public StatisticResponseDto mapToStatisticResponseDto(StatisticView statisticView) {
        return StatisticResponseDto.builder()
                .app(statisticView.getApp())
                .uri(statisticView.getUri())
                .hits(statisticView.getHits())
                .build();
    }

}
