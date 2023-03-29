package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exceptions.BookingOverlapsException;
import ru.practicum.shareit.booking.exceptions.BookingStateException;
import ru.practicum.shareit.booking.exceptions.BookingUpdateException;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.ItemUnavailableException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

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
    private BookingService bookingService;

    @Override
    protected Long getXSharerUserId() {
        return 999L;
    }

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
        performPostRequests("/bookings", bookingRequestDto)
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
        performPostRequests("/bookings", bookingRequestDto.withItemId(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with start in past test")
    void createBookingWithStartInPastTest() throws Exception {
        LocalDateTime pastStart = LocalDateTime.of(1970, 1, 1, 0, 0);
        performPostRequests("/bookings", bookingRequestDto.withStart(pastStart))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with start is null test")
    void createBookingWithStartIsNullTest() throws Exception {
        performPostRequests("/bookings", bookingRequestDto.withStart(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with end in past test")
    void createBookingWithEndInPastTest() throws Exception {
        LocalDateTime pastEnd = LocalDateTime.of(1970, 1, 1, 0, 0);
        performPostRequests("/bookings", bookingRequestDto.withEnd(pastEnd))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with end is null test")
    void createBookingWithEndIsNullTest() throws Exception {
        performPostRequests("/bookings", bookingRequestDto.withEnd(null))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking with start after end test")
    void createBookingWithStartAfterEndTest() throws Exception {
        LocalDateTime start = LocalDateTime.of(2043, 1, 1, 1, 0);
        LocalDateTime end = LocalDateTime.of(2043, 1, 1, 0, 0);
        performPostRequests("/bookings", bookingRequestDto.withStart(start).withEnd(end))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, never()).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking overlaps others test")
    void createBookingOverlapsOthersTest() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new BookingOverlapsException());
        performPostRequests("/bookings", bookingRequestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking of unavailable item test")
    void createBookingOfUnavailableItemTest() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new ItemUnavailableException(expectedItemId));
        performPostRequests("/bookings", bookingRequestDto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking of not existing item test")
    void createBookingOfNotExistingItemTest() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new ItemNotFoundException(expectedItemId));
        performPostRequests("/bookings", bookingRequestDto)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Create booking from not existing user test")
    void createBookingFromNotExistingUserTest() throws Exception {
        when(bookingService.createBooking(anyLong(), any()))
                .thenThrow(new UserNotFoundException(getXSharerUserId()));
        performPostRequests("/bookings", bookingRequestDto)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).createBooking(anyLong(), any());
    }

    @Test
    @DisplayName("Update booking test")
    void updateBookingTest() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingResponseDto.withStatus(BookingStatus.APPROVED));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("approved", Lists.list("true"));
        performPatchRequests("/bookings/" + expectedBookingId, bookingRequestDto, params)
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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("approved", Lists.list("true"));
        performPatchRequests("/bookings/" + expectedBookingId, bookingRequestDto, params)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).updateBooking(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    @DisplayName("Get booking test")
    void getBookingTest() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong())).thenReturn(bookingResponseDto);
        performGetRequests("/bookings/" + expectedBookingId, new LinkedMultiValueMap<>())
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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("from", Lists.list("0"));
        params.put("size", Lists.list("1"));
        performGetRequests("/bookings", params)
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
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("from", Lists.list("0"));
        params.put("size", Lists.list("1"));
        performGetRequests("/bookings/owner", params)
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

    @Test
    @DisplayName("Get all item owner bookings wrong state test")
    void getAllItemOwnerBookingsWrongStateTest() throws Exception {
        when(bookingService.getAllBookingsForOwnerItems(anyLong(), anyString(), anyInt(), anyInt()))
                .thenThrow(new BookingStateException("wrong state"));
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("from", Lists.list("0"));
        params.put("size", Lists.list("1"));
        performGetRequests("/bookings/owner", params)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
        verify(bookingService, times(1)).getAllBookingsForOwnerItems(anyLong(), anyString(), anyInt(), anyInt());
    }
}
