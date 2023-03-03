package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.config.ErrorResponse;
import ru.practicum.shareit.item.exceptions.*;

@RestControllerAdvice
public class ItemExceptionController {

    @ResponseBody
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse itemNotFoundExceptionHandler(ItemNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ItemCreateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse itemCreateExceptionHandler(ItemCreateException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(ItemUnavailableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse itemUnavailableExceptionHandler(ItemUnavailableException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(WrongItemOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse wrongItemOwnerExceptionHandler(WrongItemOwnerException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(CommentCreateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse commentCreateExceptionHandler(CommentCreateException e) {
        return new ErrorResponse(e.getMessage());
    }
}
