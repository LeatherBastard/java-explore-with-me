package ru.practicum.endpointstatistic.service;

import ru.practicum.endpointstatistic.dto.EndpointStatisticRequestDto;
import ru.practicum.endpointstatistic.dto.EndpointStatisticResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointStatisticService {
    void addHit(EndpointStatisticRequestDto endpointStatisticRequestDto);

    List<EndpointStatisticResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
