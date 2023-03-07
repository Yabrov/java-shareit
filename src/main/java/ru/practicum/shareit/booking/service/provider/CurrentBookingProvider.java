package ru.practicum.shareit.booking.service.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.repository.BookingRepository;

import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CurrentBookingProvider implements BookingProvider {

    private final BookingRepository bookingRepository;

    @Override
    public Collection<Booking> getAllBookingsOfUser(Long userId, BookingState state) {
        return bookingRepository.findAllCurrentBookingsOfUser(userId);
    }

    @Override
    public Collection<Booking> getAllBookingsForOwnerItems(List<Long> itemIds, BookingState state) {
        return bookingRepository.findAllCurrentBookingsOfItems(itemIds);
    }
}