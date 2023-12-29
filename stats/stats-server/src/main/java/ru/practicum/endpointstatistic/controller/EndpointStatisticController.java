package ru.practicum.endpointstatistic.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.endpointstatistic.dto.EndpointStatisticRequestDto;
import ru.practicum.endpointstatistic.dto.EndpointStatisticResponseDto;
import ru.practicum.endpointstatistic.service.EndpointStatisticService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class EndpointStatisticController {


    private static final String LOGGER_GET_STATS_MESSAGE = "Returning stats by start {} , end {} for uries {} and unique is {}";

    private static final String LOGGER_ADD_HIT_MESSAGE = "Adding hit with ip {} to uri {}";

    private final EndpointStatisticService endpointStatisticService;


    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody @Validated EndpointStatisticRequestDto endpointStatisticRequestDto) {
        log.info(LOGGER_ADD_HIT_MESSAGE, endpointStatisticRequestDto.getIp(), endpointStatisticRequestDto.getUri());
        endpointStatisticService.addHit(endpointStatisticRequestDto);
    }


    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<EndpointStatisticResponseDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end, @RequestParam(required = false) List<String> uris, @RequestParam(defaultValue = "false") boolean unique) {
        log.info(LOGGER_GET_STATS_MESSAGE, start, end, uris, unique);
        return endpointStatisticService.getStats(start, end, uris, unique);
    }


}
