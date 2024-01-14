package ru.practicum.exception;

public class EventWrongStateException extends RuntimeException {
    public EventWrongStateException(int eventId, String state) {
        super(String.format("Event with id %d has state %s", eventId, state));
    }
}
