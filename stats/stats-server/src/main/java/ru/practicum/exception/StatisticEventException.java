package ru.practicum.exception;

import java.time.LocalDateTime;

import static ru.practicum.statistic.mapper.StatisticMapper.formatter;

public class StatisticEventException extends RuntimeException {
    public StatisticEventException(LocalDateTime startDate, LocalDateTime endDate) {
        super(String.format("Dates for stats search are wrong," +
                "startDate: %s endDate: %s", startDate.format(formatter), endDate.format(formatter)));
    }

}
