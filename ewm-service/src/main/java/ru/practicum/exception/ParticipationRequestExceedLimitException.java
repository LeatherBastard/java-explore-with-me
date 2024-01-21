package ru.practicum.exception;

public class ParticipationRequestExceedLimitException extends RuntimeException {
    public ParticipationRequestExceedLimitException(int eventId, int limit) {
        super(String.format("Event with id %d exceeds participation limit in %d people", eventId, limit));
    }
}
