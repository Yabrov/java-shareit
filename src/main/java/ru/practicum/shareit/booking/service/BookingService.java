package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.Collection;

public interface BookingService {

    BookingResponseDto createBooking(Long userId, BookingRequestDto bookingRequestDto);

    BookingResponseDto updateBooking(Long userId, Long bookingId, Boolean approved);

    BookingResponseDto getBooking(Long userId, Long bookingId);

    Collection<BookingResponseDto> getAllBookingsOfUser(Long userId, String state);

    Collection<BookingResponseDto> getAllBookingsForOwnerItems(Long userId, String state);
}
