package ru.practicum.shareit.request.exceptions;

public class InvalidPaginationParamsException extends RuntimeException {

    private static final String MES_PATTERN = "Pagination params [from=%s, size=%s] are invalid";

    public InvalidPaginationParamsException(Integer from, Integer size) {
        super(String.format(MES_PATTERN, from, size));
    }
}
