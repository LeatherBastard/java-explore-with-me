package ru.practicum.exception;

import java.time.LocalDateTime;

import static ru.practicum.event.mapper.EventMapper.formatter;

public class EventWrongDateRangeException extends RuntimeException {
    public EventWrongDateRangeException(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        super(String.format("Search query has range start %s and range end %s", rangeStart.format(formatter), rangeEnd.format(formatter)));
    }
}
