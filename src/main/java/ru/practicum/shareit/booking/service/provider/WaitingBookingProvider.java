package ru.practicum.shareit.booking.service.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class WaitingBookingProvider implements BookingProvider {

    private final BookingRepository bookingRepository;

    @Override
    public Collection<Booking> getAllBookingsOfUser(Long userId, BookingState state) {
        return bookingRepository.findAllBookingsOfUserWithStatus(userId, BookingStatus.WAITING);
    }

    @Override
    public Collection<Booking> getAllBookingsForOwnerItems(List<Long> itemIds, BookingState state) {
        return bookingRepository.findAllBookingsOfItemsWithStatus(itemIds, BookingStatus.WAITING);
    }

    @Override
    public Page<Booking> getAllBookingsOfUser(Long userId, BookingState state, Pageable pageable) {
        return bookingRepository.findAllBookingsOfUserWithStatus(userId, BookingStatus.WAITING, pageable);
    }

    @Override
    public Page<Booking> getAllBookingsForOwnerItems(List<Long> itemIds, BookingState state, Pageable pageable) {
        return bookingRepository.findAllBookingsOfItemsWithStatus(itemIds, BookingStatus.WAITING, pageable);
    }
}