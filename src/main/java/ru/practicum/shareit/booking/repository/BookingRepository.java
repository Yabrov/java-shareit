package ru.practicum.shareit.booking.repository;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.util.Collection;

public interface BookingRepository {

    Booking findBookingById(Long bookingId);

    Booking saveBooking(Booking booking);

    Collection<Booking> findAllBookingsOfUserWithStatus(Long userId, BookingStatus status);

    Collection<Booking> findAllFutureBookingsOfUser(Long userId);

    Collection<Booking> findAllPastBookingsOfUser(Long userId);

    Collection<Booking> findAllCurrentBookingsOfUser(Long userId);

    Collection<Booking> findAllBookingOfUser(Long userId);

    Collection<Booking> findAllBookingsOfItemsWithStatus(Collection<Long> itemIds, BookingStatus status);

    Collection<Booking> findAllFutureBookingsOfItems(Collection<Long> itemIds);

    Collection<Booking> findAllPastBookingsOfItems(Collection<Long> itemIds);

    Collection<Booking> findAllCurrentBookingsOfItems(Collection<Long> itemIds);

    Collection<Booking> findAllBookingOfItems(Collection<Long> itemIds);

    Boolean isBookingOverlapsOthers(Booking booking);
}
