package ru.practicum.shareit.booking.exceptions;

public class BookingOverlapsException extends RuntimeException {

    public BookingOverlapsException() {
        super("Booking overlaps others");
    }
}
