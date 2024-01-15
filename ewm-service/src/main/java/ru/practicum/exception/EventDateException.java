package ru.practicum.exception;

public class EventDateException extends RuntimeException {
    public EventDateException(String message, int eventId) {
        super(String.format(message, eventId));
    }
}
