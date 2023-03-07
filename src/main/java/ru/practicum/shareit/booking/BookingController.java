package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        return bookingService.createBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId,
            @RequestParam Boolean approved) {
        return bookingService.updateBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingResponseDto> getAllBookingsOfUser(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingsOfUser(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingResponseDto> getAllBookingsForOwnerItems(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingsForOwnerItems(userId, state);
    }
}
