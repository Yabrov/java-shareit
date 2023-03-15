package ru.practicum.shareit.booking.service.provider;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;

import java.util.Collection;
import java.util.List;

public interface BookingProvider {

    Collection<Booking> getAllBookingsOfUser(Long userId, BookingState state);

    Collection<Booking> getAllBookingsForOwnerItems(List<Long> itemIds, BookingState state);

    Page<Booking> getAllBookingsOfUser(Long userId, BookingState state, Pageable pageable);

    Page<Booking> getAllBookingsForOwnerItems(List<Long> itemIds, BookingState state, Pageable pageable);
}
