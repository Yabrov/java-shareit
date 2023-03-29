package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.util.Collection;

public interface BookingRepository {

    Booking findBookingById(Long bookingId);

    Booking saveBooking(Booking booking);

    Collection<Booking> findAllBookingsOfUserWithStatus(Long userId, BookingStatus status);

    Page<Booking> findAllBookingsOfUserWithStatus(Long userId, BookingStatus status, Pageable pageable);

    Collection<Booking> findAllFutureBookingsOfUser(Long userId);

    Page<Booking> findAllFutureBookingsOfUser(Long userId, Pageable pageable);

    Collection<Booking> findAllPastBookingsOfUser(Long userId);

    Page<Booking> findAllPastBookingsOfUser(Long userId, Pageable pageable);

    Collection<Booking> findAllCurrentBookingsOfUser(Long userId);

    Page<Booking> findAllCurrentBookingsOfUser(Long userId, Pageable pageable);

    Collection<Booking> findAllBookingOfUser(Long userId);

    Page<Booking> findAllBookingOfUser(Long userId, Pageable pageable);

    Collection<Booking> findAllBookingsOfItemsWithStatus(Collection<Long> itemIds, BookingStatus status);

    Page<Booking> findAllBookingsOfItemsWithStatus(Collection<Long> itemIds, BookingStatus status, Pageable pageable);

    Collection<Booking> findAllFutureBookingsOfItems(Collection<Long> itemIds);

    Page<Booking> findAllFutureBookingsOfItems(Collection<Long> itemIds, Pageable pageable);

    Collection<Booking> findAllPastBookingsOfItems(Collection<Long> itemIds);

    Page<Booking> findAllPastBookingsOfItems(Collection<Long> itemIds, Pageable pageable);

    Collection<Booking> findAllCurrentBookingsOfItems(Collection<Long> itemIds);

    Page<Booking> findAllCurrentBookingsOfItems(Collection<Long> itemIds, Pageable pageable);

    Collection<Booking> findAllBookingOfItems(Collection<Long> itemIds);

    Page<Booking> findAllBookingOfItems(Collection<Long> itemIds, Pageable pageable);

    Boolean isBookingOverlapsOthers(Booking booking);
}
