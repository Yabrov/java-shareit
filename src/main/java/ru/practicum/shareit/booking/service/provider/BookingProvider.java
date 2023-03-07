package ru.practicum.shareit.booking.service.provider;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;

import java.util.Collection;
import java.util.List;

public interface BookingProvider {

    Collection<Booking> getAllBookingsOfUser(Long userId, BookingState state);

    Collection<Booking> getAllBookingsForOwnerItems(List<Long> itemIds, BookingState state);
}
