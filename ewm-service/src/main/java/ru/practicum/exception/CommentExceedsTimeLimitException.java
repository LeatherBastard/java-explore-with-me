package ru.practicum.exception;

public class CommentExceedsTimeLimitException extends RuntimeException {
    public CommentExceedsTimeLimitException(int comId) {
        super(String.format("Cannot update comment with id %d," +
                "because comment created date has to be not later than 5 minutes before editing", comId));
    }

}
