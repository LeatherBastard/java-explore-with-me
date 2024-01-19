package ru.practicum.exception;

public class CommentWrongOwnerException extends RuntimeException{
    public CommentWrongOwnerException(int comId, int userId) {
        super(String.format("Comment with id %d has an owner with id %d", comId, userId));
    }
}
