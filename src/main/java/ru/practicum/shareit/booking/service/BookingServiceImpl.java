package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingState;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.BookingOverlapsException;
import ru.practicum.shareit.booking.exceptions.BookingStateException;
import ru.practicum.shareit.booking.exceptions.BookingUpdateException;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.provider.BookingProvider;
import ru.practicum.shareit.config.PageBuilder;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.ItemUnavailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final Converter<Booking, BookingResponseDto> bookingResponseMapper;
    private final Converter<BookingRequestDto, Booking> bookingRequestMapper;
    private final BookingProvider bookingProviderSelector;
    private final PageBuilder pageBuilder;

    @Transactional
    @Override
    public BookingResponseDto createBooking(Long userId, BookingRequestDto bookingRequestDto) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        Item item = itemRepository.findItemById(bookingRequestDto.getItemId());
        if (item == null) {
            throw new ItemNotFoundException(bookingRequestDto.getItemId());
        }
        if (!item.getAvailable()) {
            throw new ItemUnavailableException(bookingRequestDto.getItemId());
        }
        if (user.equals(item.getOwner())) {
            throw new ItemNotFoundException(item.getId());
        }
        Booking booking = bookingRequestMapper.convert(bookingRequestDto);
        booking.setBooker(user);
        booking.setItem(item);
        if (bookingRepository.isBookingOverlapsOthers(booking)) {
            throw new BookingOverlapsException();
        }
        return bookingResponseMapper.convert(bookingRepository.saveBooking(booking));
    }

    @Transactional
    @Override
    public BookingResponseDto updateBooking(Long userId, Long bookingId, Boolean approved) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null || !booking.getItem().getOwner().equals(user)) {
            throw new BookingNotFoundException(bookingId);
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new BookingUpdateException("Booking already has been approved");
        }
        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return bookingResponseMapper.convert(bookingRepository.saveBooking(booking));
    }

    @Override
    public BookingResponseDto getBooking(Long userId, Long bookingId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        Booking booking = bookingRepository.findBookingById(bookingId);
        if (booking == null) {
            throw new BookingNotFoundException(bookingId);
        }
        if (!booking.getBooker().equals(user) && !booking.getItem().getOwner().equals(user)) {
            throw new BookingNotFoundException(bookingId);
        }
        return bookingResponseMapper.convert(booking);
    }

    @Override
    public Collection<BookingResponseDto> getAllBookingsOfUser(Long userId, String state, Integer from, Integer size) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new BookingStateException(state);
        }
        if (from == null || size == null) {
            return bookingProviderSelector
                    .getAllBookingsOfUser(userId, bookingState)
                    .stream()
                    .map(bookingResponseMapper::convert)
                    .collect(Collectors.toList());
        }
        Pageable pageable = pageBuilder.build(from, size, Sort.by(Sort.Direction.DESC, "start"));
        return bookingProviderSelector
                .getAllBookingsOfUser(userId, bookingState, pageable)
                .stream()
                .map(bookingResponseMapper::convert)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingResponseDto> getAllBookingsForOwnerItems(Long userId, String state, Integer from, Integer size) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        List<Long> itemIds = itemRepository
                .findAllItems(userId)
                .stream()
                .map(Item::getId)
                .collect(Collectors.toList());
        if (itemIds.isEmpty()) {
            return Collections.emptyList();
        }
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new BookingStateException(state);
        }
        if (from == null || size == null) {
            return bookingProviderSelector
                    .getAllBookingsForOwnerItems(itemIds, bookingState)
                    .stream()
                    .map(bookingResponseMapper::convert)
                    .collect(Collectors.toList());
        }
        Pageable pageable = pageBuilder.build(from, size, Sort.by(Sort.Direction.DESC, "start"));
        return bookingProviderSelector
                .getAllBookingsForOwnerItems(itemIds, bookingState, pageable)
                .stream()
                .map(bookingResponseMapper::convert)
                .collect(Collectors.toList());
    }
}
