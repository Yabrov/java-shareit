package ru.practicum.shareit.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.config.ErrorResponse;
import ru.practicum.shareit.request.exceptions.InvalidPaginationParamsException;
import ru.practicum.shareit.request.exceptions.ItemRequestNotFoundException;

@RestControllerAdvice
public class ItemRequestExceptionController {

    @ResponseBody
    @ExceptionHandler(ItemRequestNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse itemRequestNotFoundExceptionHandler(ItemRequestNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(InvalidPaginationParamsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse invalidPaginationParamsExceptionHandler(InvalidPaginationParamsException e) {
        return new ErrorResponse(e.getMessage());
    }
}
