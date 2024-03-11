package ru.practicum.exception;

public class EventNotPublishedException extends RuntimeException {
    public EventNotPublishedException(int eventId) {
        super(String.format("Event with id %d has not published yet", eventId));
    }
}
