package ru.practicum.exception;

public class ParticipationRequestWrongIdException extends RuntimeException {
    public ParticipationRequestWrongIdException(int actualId, int expectedId) {
        super(String.format("Event id of request is %d,but event id in query was %d", actualId, expectedId));
    }
}
