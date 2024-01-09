package ru.practicum.exception;

public class ParticipationRequestAlreadyAddedException extends RuntimeException {
    public ParticipationRequestAlreadyAddedException(int eventId, int userId) {
        super(String.format("Event with id %d already has a participant with id %d", eventId, userId));
    }

}
