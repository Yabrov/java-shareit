package ru.practicum.shareit.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.user.exception.EmailUniqueViolationException;
import ru.practicum.shareit.user.exception.NullEmailException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

@RestControllerAdvice
public class UserExceptionController {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String userNotFoundHandler(UserNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(EmailUniqueViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String emailConstraintViolationHandler(EmailUniqueViolationException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String validationExceptionHandler(MethodArgumentNotValidException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(NullEmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String nullEmailExceptionHandler(NullEmailException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String internalErrorHandler(Exception e) {
        return e.getMessage();
    }
}
