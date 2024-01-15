package ru.practicum.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message, int id) {
        super(String.format(message, id));
    }
}
