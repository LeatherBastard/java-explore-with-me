package ru.practicum.statistic.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statistic.dto.StatisticRequestDto;
import ru.practicum.statistic.dto.StatisticResponseDto;
import ru.practicum.statistic.service.StatisticService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class StatisticController {


    private static final String LOGGER_GET_STATS_MESSAGE = "Returning stats by start {} , end {} for uries {} and unique is {}";

    private static final String LOGGER_ADD_HIT_MESSAGE = "Adding hit with ip {} to uri {}";

    private final StatisticService statisticService;


    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addHit(@RequestBody @Validated StatisticRequestDto statisticRequestDto) {
        log.info(LOGGER_ADD_HIT_MESSAGE, statisticRequestDto.getIp(), statisticRequestDto.getUri());
        statisticService.addHit(statisticRequestDto);
    }


    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<StatisticResponseDto> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end, @RequestParam(required = false) List<String> uris, @RequestParam(defaultValue = "false") boolean unique) {
        log.info(LOGGER_GET_STATS_MESSAGE, start, end, uris, unique);
        return statisticService.getStats(start, end, uris, unique);
    }


}
