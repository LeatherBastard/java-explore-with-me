package ru.practicum.exception;

public class ParticipationRequestOwnerParticipantException extends RuntimeException {
    public ParticipationRequestOwnerParticipantException(int eventId, int userId) {
        super(String.format("Event with id %d has an owner with id %d", eventId, userId));
    }
}
