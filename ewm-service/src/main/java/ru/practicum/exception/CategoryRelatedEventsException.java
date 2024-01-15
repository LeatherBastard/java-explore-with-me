package ru.practicum.exception;

public class CategoryRelatedEventsException extends RuntimeException {
    public CategoryRelatedEventsException(int catId) {
        super(String.format("There are events that has a category with id: %d", catId));
    }

}
