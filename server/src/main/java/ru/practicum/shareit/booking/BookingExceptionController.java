package ru.practicum.shareit.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.BookingOverlapsException;
import ru.practicum.shareit.booking.exceptions.BookingStateException;
import ru.practicum.shareit.booking.exceptions.BookingUpdateException;
import ru.practicum.shareit.config.ErrorResponse;

@RestControllerAdvice
public class BookingExceptionController {

    @ResponseBody
    @ExceptionHandler(BookingNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse bookingNotFoundExceptionHandler(BookingNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({
            BookingUpdateException.class,
            BookingStateException.class,
            BookingOverlapsException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse bookingBadRequestHandler(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}
