package ru.practicum.exception;

public class EventUpdateDateException extends RuntimeException {
    public EventUpdateDateException(String message, int eventId) {
        super(String.format(message, eventId));
    }
}
