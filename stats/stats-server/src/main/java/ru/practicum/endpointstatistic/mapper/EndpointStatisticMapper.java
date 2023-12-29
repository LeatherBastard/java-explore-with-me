package ru.practicum.endpointstatistic.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.endpointstatistic.dto.EndpointStatisticRequestDto;
import ru.practicum.endpointstatistic.model.EndpointStatistic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class EndpointStatisticMapper {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    public EndpointStatistic mapToEndpointStatistic(EndpointStatisticRequestDto endpointStatisticRequestDto) {
        return EndpointStatistic.builder()
                .app(endpointStatisticRequestDto.getApp())
                .uri(endpointStatisticRequestDto.getUri())
                .ip(endpointStatisticRequestDto.getIp())
                .timestamp(LocalDateTime.parse(endpointStatisticRequestDto.getTimestamp(), formatter))
                .build();
    }
}
