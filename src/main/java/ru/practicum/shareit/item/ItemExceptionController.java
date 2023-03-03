package ru.practicum.shareit.item;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exceptions.ItemCreateException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongItemOwnerException;

@RestControllerAdvice
public class ItemExceptionController {

    @ResponseBody
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String itemNotFoundExceptionHandler(ItemNotFoundException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(ItemCreateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String itemCreateExceptionHandler(ItemCreateException e) {
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(WrongItemOwnerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String wrongItemOwnerExceptionHandler(WrongItemOwnerException e) {
        return e.getMessage();
    }
}
