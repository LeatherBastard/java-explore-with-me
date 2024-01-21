package ru.practicum.exception;

import java.util.List;

public class CompilationEventsNotFoundException extends RuntimeException {
    public CompilationEventsNotFoundException(List<Integer> ids) {
        super(String.format("One of the ids was not found: %s", ids.toString()));
    }
}
