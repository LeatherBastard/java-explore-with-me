package ru.practicum.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    private static final String BAD_REQUEST_STATUS = "BAD_REQUEST";
    private static final String BAD_REQUEST_REASON = "Incorrectly made request.";

    private static final String NOT_FOUND_STATUS = "NOT_FOUND";
    private static final String NOT_FOUND_REASON = "The required object was not found.";

    private static final String INTERNAL_SERVER_ERROR_STATUS = "INTERNAL_SERVER_ERROR";
    private static final String INTERNAL_SERVER_ERROR_REASON = "Internal server error.";

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(final ConstraintViolationException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(final MissingServletRequestParameterException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEventWrongDateRangeException(final EventWrongDateRangeException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEventUpdateDateException(final EventDateException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEventUpdateStateException(final EventUpdateStateException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipationRequestExceedLimitException(final ParticipationRequestExceedLimitException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipationRequestEventNotPublishedException(final ParticipationRequestEventNotPublishedException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipationRequestAlreadyAddedException(final ParticipationRequestAlreadyAddedException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleParticipationRequestOwnerParticipantException(final ParticipationRequestOwnerParticipantException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserEmailOccupiedException(final UserEmailOccupiedException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryNameOccupiedException(final CategoryNameOccupiedException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryRelatedEventsException(final CategoryRelatedEventsException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException exception) {
        return new ApiError(NOT_FOUND_STATUS, NOT_FOUND_REASON, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEventWrongStateException(final EventWrongStateException exception) {
        return new ApiError(NOT_FOUND_STATUS, NOT_FOUND_REASON, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable exception) {
        return new ApiError(INTERNAL_SERVER_ERROR_STATUS, INTERNAL_SERVER_ERROR_REASON, exception.toString(), LocalDateTime.now());
    }


}
