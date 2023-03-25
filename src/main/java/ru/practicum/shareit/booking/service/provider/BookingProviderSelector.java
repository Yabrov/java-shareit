package ru.practicum.shareit.booking.service.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BookingProviderSelector implements BookingProvider {

    private final Map<BookingState, BookingProvider> providersMap;

    @Override
    public Collection<Booking> getAllBookingsOfUser(Long userId, BookingState state) {
        return providersMap.get(state).getAllBookingsOfUser(userId, state);
    }

    @Override
    public Collection<Booking> getAllBookingsForOwnerItems(List<Long> itemIds, BookingState state) {
        return providersMap.get(state).getAllBookingsForOwnerItems(itemIds, state);
    }

    @Override
    public Page<Booking> getAllBookingsOfUser(Long userId, BookingState state, Pageable pageable) {
        return providersMap.get(state).getAllBookingsOfUser(userId, state, pageable);
    }

    @Override
    public Page<Booking> getAllBookingsForOwnerItems(List<Long> itemIds, BookingState state, Pageable pageable) {
        return providersMap.get(state).getAllBookingsForOwnerItems(itemIds, state, pageable);
    }
}
