package ru.practicum.shareit.booking.service.provider;

import lombok.RequiredArgsConstructor;
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
}
