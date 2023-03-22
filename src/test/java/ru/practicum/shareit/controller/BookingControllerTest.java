package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
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
import ru.practicum.shareit.booking.exceptions.BookingOverlapsException;
import ru.practicum.shareit.booking.exceptions.BookingUpdateException;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.ItemUnavailableException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

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
        when(bookingService.createBooking(anyLong(), any())).thenReturn(bookingResponseDto);
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

    @Test
    @DisplayName("Create booking with item id is null test")
    void createBookingWithItemIdIsNullTest() throws Exception {
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto.withItemId(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with start in past test")
    void createBookingWithStartInPastTest() throws Exception {
        LocalDateTime pastStart = LocalDateTime.of(1970, 1, 1, 0, 0);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto.withStart(pastStart)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with start is null test")
    void createBookingWithStartIsNullTest() throws Exception {
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto.withStart(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with end in past test")
    void createBookingWithEndInPastTest() throws Exception {
        LocalDateTime pastEnd = LocalDateTime.of(1970, 1, 1, 0, 0);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto.withEnd(pastEnd)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with end is null test")
    void createBookingWithEndIsNullTest() throws Exception {
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto.withEnd(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with start after end test")
    void createBookingWithStartAfterEndTest() throws Exception {
        LocalDateTime start = LocalDateTime.of(2043, 1, 1, 1, 0);
        LocalDateTime end = LocalDateTime.of(2043, 1, 1, 0, 0);
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto.withStart(start).withEnd(end)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking overlaps others test")
    void createBookingOverlapsOthersTest() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new BookingOverlapsException());
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking of unavailable item test")
    void createBookingOfUnavailableItemTest() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new ItemUnavailableException(expectedItemId));
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking of not existing item test")
    void createBookingOfNotExistingItemTest() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new ItemNotFoundException(expectedItemId));
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking from not existing user test")
    void createBookingFromNotExistingUserTest() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new UserNotFoundException(xSharerUserId));
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .content(mapper.writeValueAsString(bookingRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Update booking test")
    void updateBookingTest() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingResponseDto.withStatus(BookingStatus.APPROVED));
        mvc.perform(patch("/bookings/" + expectedBookingId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .queryParam("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookingId), Long.class))
                .andExpect(jsonPath("$.start", is(bookingResponseDto.getStart().format(formatter))))
                .andExpect(jsonPath("$.end", is(bookingResponseDto.getEnd().format(formatter))))
                .andExpect(jsonPath("$.status", is(BookingStatus.APPROVED.name())))
                .andExpect(jsonPath("$.item.id", is(bookingResponseDto.getItemDto().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingResponseDto.getBookerDto().getId()), Long.class));
        verify(bookingService, times(1)).updateBooking(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @DisplayName("Update already approved booking test")
    void updateAlreadyApprovedBookingTest() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(new BookingUpdateException(""));
        mvc.perform(patch("/bookings/" + expectedBookingId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .queryParam("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).updateBooking(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @DisplayName("Get booking test")
    void getBookingTest() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(bookingResponseDto);
        mvc.perform(get("/bookings/" + expectedBookingId)
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(expectedBookingId), Long.class))
                .andExpect(jsonPath("$.start", is(bookingResponseDto.getStart().format(formatter))))
                .andExpect(jsonPath("$.end", is(bookingResponseDto.getEnd().format(formatter))))
                .andExpect(jsonPath("$.status", is(bookingResponseDto.getStatus().name())))
                .andExpect(jsonPath("$.item.id", is(bookingResponseDto.getItemDto().getId()), Long.class))
                .andExpect(jsonPath("$.booker.id", is(bookingResponseDto.getBookerDto().getId()), Long.class));
        verify(bookingService, times(1)).getBooking(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Get all bookings of user test")
    void getAllBookingsOfUserTest() throws Exception {
        when(bookingService.getAllBookingsOfUser(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(Lists.list(bookingResponseDto));
        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .queryParam("from", "0")
                        .queryParam("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id", is(expectedBookingId), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingResponseDto.getStart().format(formatter))))
                .andExpect(jsonPath("$[0].end", is(bookingResponseDto.getEnd().format(formatter))))
                .andExpect(jsonPath("$[0].status", is(bookingResponseDto.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", is(bookingResponseDto.getItemDto().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingResponseDto.getBookerDto().getId()), Long.class));
        verify(bookingService, times(1)).getAllBookingsOfUser(anyLong(), anyString(), anyInt(), anyInt());
    }

    @Test
    @DisplayName("Get all item owner bookings test")
    void getAllItemOwnerBookingsTest() throws Exception {
        when(bookingService.getAllBookingsForOwnerItems(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(Lists.list(bookingResponseDto));
        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", xSharerUserId)
                        .queryParam("from", "0")
                        .queryParam("size", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id", is(expectedBookingId), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingResponseDto.getStart().format(formatter))))
                .andExpect(jsonPath("$[0].end", is(bookingResponseDto.getEnd().format(formatter))))
                .andExpect(jsonPath("$[0].status", is(bookingResponseDto.getStatus().name())))
                .andExpect(jsonPath("$[0].item.id", is(bookingResponseDto.getItemDto().getId()), Long.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingResponseDto.getBookerDto().getId()), Long.class));
        verify(bookingService, times(1)).getAllBookingsForOwnerItems(anyLong(), anyString(), anyInt(), anyInt());
    }
}
