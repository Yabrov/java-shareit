package ru.practicum.shareit.booking.exceptions;

public class BookingNotFoundException extends RuntimeException {

    private static final String MES_PATTERN = "Booking with id %s does not exist.";

    public BookingNotFoundException(Long id) {
        super(String.format(MES_PATTERN, id));
    }
}
