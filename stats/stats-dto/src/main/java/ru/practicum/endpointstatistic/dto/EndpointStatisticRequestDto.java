package ru.practicum.endpointstatistic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
@AllArgsConstructor
@Builder
public class EndpointStatisticRequestDto {
    @NotEmpty
    private String app;
    @NotEmpty
    private String uri;
    @NotEmpty
    private String ip;
    @NotEmpty
    private String timestamp;
}
