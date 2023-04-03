package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.config.ErrorResponse;
import ru.practicum.shareit.user.exception.EmailUniqueViolationException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

@RestControllerAdvice
public class UserExceptionController {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFoundHandler(UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(EmailUniqueViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse emailConstraintViolationHandler(EmailUniqueViolationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse internalErrorHandler(Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
