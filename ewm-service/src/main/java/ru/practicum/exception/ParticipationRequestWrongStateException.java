package ru.practicum.exception;

public class ParticipationRequestWrongStateException extends RuntimeException {
    public ParticipationRequestWrongStateException(int requestId, String state) {
        super(String.format("Participation request with id %d has state %s", requestId, state));
    }
}
