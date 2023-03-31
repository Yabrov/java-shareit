package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest extends AbstractControllerTest {

    @Autowired
    public BookingControllerTest(ObjectMapper mapper, MockMvc mvc) {
        this.mapper = mapper;
        this.mvc = mvc;
    }

    @MockBean
    private BookingClient bookingClient;

    @Override
    protected Long getXSharerUserId() {
        return 999L;
    }

    private final Long expectedBookingId = 1L;

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto(
            2L,
            LocalDateTime.of(2043, 1, 1, 10, 0, 0),
            LocalDateTime.of(2043, 1, 1, 11, 0, 0)
    );

    private final BookingResponseDto bookingResponseDto = new BookingResponseDto(expectedBookingId);

    @Getter
    @Setter
    @AllArgsConstructor
    private static class BookingResponseDto {
        private Long id;
    }

    @Test
    @DisplayName("Create valid booking test")
    void createValidBookingTest() throws Exception {
        when(bookingClient.bookItem(anyLong(), any())).thenReturn(ResponseEntity.ok().body(bookingResponseDto));
        performPostRequests("/bookings", bookingRequestDto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookingId), Long.class));
        verify(bookingClient, times(1)).bookItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with item id is null test")
    void createBookingWithItemIdIsNullTest() throws Exception {
        performPostRequests("/bookings", bookingRequestDto.withItemId(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingClient, never()).bookItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with start in past test")
    void createBookingWithStartInPastTest() throws Exception {
        LocalDateTime pastStart = LocalDateTime.of(1970, 1, 1, 0, 0);
        performPostRequests("/bookings", bookingRequestDto.withStart(pastStart))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingClient, never()).bookItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with start is null test")
    void createBookingWithStartIsNullTest() throws Exception {
        performPostRequests("/bookings", bookingRequestDto.withStart(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingClient, never()).bookItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with end in past test")
    void createBookingWithEndInPastTest() throws Exception {
        LocalDateTime pastEnd = LocalDateTime.of(1970, 1, 1, 0, 0);
        performPostRequests("/bookings", bookingRequestDto.withEnd(pastEnd))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingClient, never()).bookItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with end is null test")
    void createBookingWithEndIsNullTest() throws Exception {
        performPostRequests("/bookings", bookingRequestDto.withEnd(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingClient, never()).bookItem(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with start after end test")
    void createBookingWithStartAfterEndTest() throws Exception {
        LocalDateTime start = LocalDateTime.of(2043, 1, 1, 1, 0);
        LocalDateTime end = LocalDateTime.of(2043, 1, 1, 0, 0);
        performPostRequests("/bookings", bookingRequestDto.withStart(start).withEnd(end))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingClient, never()).bookItem(anyLong(), any());
    }

    @Test
    @DisplayName("Update booking test")
    void updateBookingTest() throws Exception {
        when(bookingClient.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(ResponseEntity.ok().body(bookingResponseDto));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("approved", Lists.list("true"));
        performPatchRequests("/bookings/" + expectedBookingId, bookingRequestDto, params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookingId), Long.class));
        verify(bookingClient, times(1)).updateBooking(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @DisplayName("Get booking test")
    void getBookingTest() throws Exception {
        when(bookingClient.getBooking(anyLong(), anyLong())).thenReturn(ResponseEntity.ok().body(bookingResponseDto));
        performGetRequests("/bookings/" + expectedBookingId, new LinkedMultiValueMap<>())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookingId), Long.class));
        verify(bookingClient, times(1)).getBooking(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get all bookings of user test")
    void getAllBookingsOfUserTest() throws Exception {
        when(bookingClient.getBookings(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().body(Lists.list(bookingRequestDto)));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("from", Lists.list("0"));
        params.put("size", Lists.list("1"));
        performGetRequests("/bookings", params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
        verify(bookingClient, times(1)).getBookings(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Get all item owner bookings test")
    void getAllItemOwnerBookingsTest() throws Exception {
        when(bookingClient.getBookingsOwner(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(ResponseEntity.ok().body(Lists.list(bookingResponseDto)));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("from", Lists.list("0"));
        params.put("size", Lists.list("1"));
        performGetRequests("/bookings/owner", params)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
        verify(bookingClient, times(1)).getBookingsOwner(anyLong(), anyString(), anyInt(), anyInt());
    }
}
