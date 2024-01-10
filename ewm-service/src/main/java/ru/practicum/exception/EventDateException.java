package ru.practicum.exception;

public class EventDateException extends RuntimeException {
    public EventDateException(int eventId) {
        super(String.format("Cannot publish event with id %d," +
                " because event date has to be not earlier than hour before publication", eventId));
    }
}
