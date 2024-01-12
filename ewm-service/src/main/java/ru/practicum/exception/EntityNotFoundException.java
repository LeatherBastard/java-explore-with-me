package ru.practicum.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message, int... ids) {
        super(String.format(message, ids));
    }
}
