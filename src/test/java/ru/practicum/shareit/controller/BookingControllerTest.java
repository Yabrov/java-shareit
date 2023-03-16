package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private final Long xSharerUserId = 999L;
    private final Long expectedBookingId = 1L;
    private final Long expectedItemId = 33L;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private final ItemDto itemDto = new ItemDto(
            expectedItemId,
            "test_name",
            "test_description",
            Boolean.FALSE,
            null,
            null,
            null,
            Collections.emptyList()
    );

    private final UserDto userDto = new UserDto(
            1L,
            "test_name",
            "test_email@test.domain.com"
    );

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto(
            expectedItemId,
            LocalDateTime.of(2043, 1, 1, 10, 0, 0),
            LocalDateTime.of(2043, 1, 1, 11, 0, 0)
    );

    private final BookingResponseDto bookingResponseDto = new BookingResponseDto(
            expectedBookingId,
            LocalDateTime.of(2043, 1, 1, 10, 0, 0),
            LocalDateTime.of(2043, 1, 1, 11, 0, 0),
            BookingStatus.WAITING,
            itemDto,
            userDto
    );

    @Test
    @DisplayName("Create valid booking test")
    void createValidBookingTest() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenReturn(bookingResponseDto.withId(expectedBookingId));
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookingId), Long.class))
                .andExpect(jsonPath("$.start", is(bookingResponseDto.getStart().format(formatter))))
                .andExpect(jsonPath("$.end", is(bookingResponseDto.getEnd().format(formatter))))
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().name())))
                .andExpect(jsonPath("$.item.id", is(bookingResponseDto.getItemDto().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingResponseDto.getBookerDto().getId()), Long.class));
        verify(bookingService, times(1)).createBooking(anyLong(), any());
    }
}
