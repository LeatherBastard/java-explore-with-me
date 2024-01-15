package ru.practicum.exception;

public class CategoryNameOccupiedException extends RuntimeException {
    public CategoryNameOccupiedException(String name) {
        super(String.format("There is already a category, which has a name of: %s", name));
    }
}
