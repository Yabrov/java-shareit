package ru.practicum.shareit.booking.exceptions;

public class BookingStateException extends RuntimeException {

    private static final String MES_PATTERN = "Unknown state: %s";

    public BookingStateException(String state) {
        super(String.format(MES_PATTERN, state));
    }
}
