package ru.practicum.shareit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.service.provider.BookingProvider;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
@ComponentScan("ru.practicum.shareit.booking.service.provider")
public class BaseConfig {

    private final BookingProvider pastBookingProvider;
    private final BookingProvider futureBookingProvider;
    private final BookingProvider currentBookingProvider;
    private final BookingProvider waitingBookingProvider;
    private final BookingProvider rejectedBookingProvider;
    private final BookingProvider defaultBookingProvider;

    @Bean
    public Map<BookingState, BookingProvider> getBookingProvidersMap() {
        final Map<BookingState, BookingProvider> map = new HashMap<>();
        map.put(BookingState.PAST, pastBookingProvider);
        map.put(BookingState.FUTURE, futureBookingProvider);
        map.put(BookingState.CURRENT, currentBookingProvider);
        map.put(BookingState.WAITING, waitingBookingProvider);
        map.put(BookingState.REJECTED, rejectedBookingProvider);
        map.put(BookingState.ALL, defaultBookingProvider);
        return map;
    }
}
