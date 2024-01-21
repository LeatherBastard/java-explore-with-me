package ru.practicum.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class ApiError {
    private final String status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;

    public ApiError(String status, String reason, String message, LocalDateTime timestamp) {
        this.status = status;
        this.reason = reason;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
