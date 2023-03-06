package ru.practicum.shareit.booking.service.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BookingProviderSelector implements BookingProvider {

    private final BookingProvider pastBookingProvider;
    private final BookingProvider futureBookingProvider;
    private final BookingProvider currentBookingProvider;
    private final BookingProvider waitingBookingProvider;
    private final BookingProvider rejectedBookingProvider;
    private final BookingProvider defaultBookingProvider;

    private final Map<BookingState, BookingProvider> providers = new HashMap<>();

    @PostConstruct
    private void initMap() {
        providers.put(BookingState.PAST, pastBookingProvider);
        providers.put(BookingState.FUTURE, futureBookingProvider);
        providers.put(BookingState.CURRENT, currentBookingProvider);
        providers.put(BookingState.WAITING, waitingBookingProvider);
        providers.put(BookingState.REJECTED, rejectedBookingProvider);
        providers.put(BookingState.ALL, defaultBookingProvider);
    }

    @Override
    public Collection<Booking> getAllBookingsOfUser(Long userId, BookingState state) {
        return providers.get(state).getAllBookingsOfUser(userId, state);
    }

    @Override
    public Collection<Booking> getAllBookingsForOwnerItems(List<Long> itemIds, BookingState state) {
        return providers.get(state).getAllBookingsForOwnerItems(itemIds, state);
    }
}
