package ru.practicum.shareit.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BookingClientTest {

    private final RestTemplate rest = Mockito.mock(RestTemplate.class);

    private final BookingClient bookingClient = new BookingClient(rest);

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto(
            1L,
            LocalDateTime.of(2043, 1, 1, 10, 0, 0),
            LocalDateTime.of(2043, 1, 1, 11, 0, 0)
    );

    @BeforeEach
    void init() {
        Mockito
                .when(rest.exchange(
                        anyString(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<ResponseEntity<Object>>any(),
                        ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(ResponseEntity.ok().body(null));
        Mockito
                .when(rest.exchange(
                        anyString(),
                        ArgumentMatchers.any(),
                        ArgumentMatchers.<ResponseEntity<Object>>any(),
                        ArgumentMatchers.<Class<Object>>any(),
                        anyMap()))
                .thenReturn(ResponseEntity.ok(null));
    }

    @Test
    void bookItemTest() {
        assertThat(bookingClient.bookItem(1L, bookingRequestDto)).isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void updateBookingTest() {
        assertThat(bookingClient.updateBooking(1L, 2L, true))
                .isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any());
    }

    @Test
    void getAllBookingTest() {
        assertThat(bookingClient.getBookings(1L, "ALL", 0, 1))
                .isEqualTo(ResponseEntity.ok(null));
        verify(rest, times(1)).exchange(anyString(),
                ArgumentMatchers.any(),
                ArgumentMatchers.<ResponseEntity<Object>>any(),
                ArgumentMatchers.<Class<Object>>any(),
                anyMap());
    }
}
