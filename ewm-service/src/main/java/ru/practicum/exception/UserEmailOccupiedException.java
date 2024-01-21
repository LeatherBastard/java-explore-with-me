package ru.practicum.exception;

public class UserEmailOccupiedException extends RuntimeException {
    public UserEmailOccupiedException(String email) {
        super(String.format("There is already a user, who has an email of: %s", email));
    }
}
