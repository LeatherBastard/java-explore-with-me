package ru.practicum.exception;

public class ParticipationRequestEventNotPublishedException extends RuntimeException {
    public ParticipationRequestEventNotPublishedException(int eventId) {
        super(String.format("Event with id %d has not published yet", eventId));
    }
}
