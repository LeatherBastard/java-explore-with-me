package ru.practicum.exception;

import java.time.LocalDateTime;

import static ru.practicum.event.mapper.EventMapper.formatter;

public class WrongDateRangeException extends RuntimeException {
    public WrongDateRangeException(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        super(String.format("Search query has range start %s and range end %s", rangeStart.format(formatter), rangeEnd.format(formatter)));
    }
}
