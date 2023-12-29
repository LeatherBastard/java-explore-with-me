package ru.practicum.endpointstatistic.dto;

public interface EndpointStatisticResponseDto {
    String getApp();

    String getUri();

    Integer getHits();
}
