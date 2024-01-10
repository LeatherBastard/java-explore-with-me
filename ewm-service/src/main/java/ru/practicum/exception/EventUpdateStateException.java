package ru.practicum.exception;

public class EventUpdateStateException extends RuntimeException {
    public EventUpdateStateException(String message, int eventId, String eventState) {
        super(String.format(message, eventId, eventState));
    }
}
