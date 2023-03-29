package ru.practicum.shareit.booking.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DatabaseBookingRepositoryImpl implements BookingRepository {

    private final JpaBookingRepository bookingRepository;
    private final Sort bookingSort = Sort.by(Sort.Direction.DESC, "start");

    @Override
    public Booking findBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElse(null);
    }

    @Transactional
    @Override
    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Collection<Booking> findAllBookingsOfUserWithStatus(Long userId, BookingStatus status) {
        return bookingRepository.findAllBookingsOfUserByStatus(userId, status, bookingSort);
    }

    @Override
    public Page<Booking> findAllBookingsOfUserWithStatus(Long userId, BookingStatus status, Pageable pageable) {
        return bookingRepository.findAllBookingsOfUserByStatus(userId, status, pageable);
    }

    @Override
    public Collection<Booking> findAllFutureBookingsOfUser(Long userId) {
        return bookingRepository.findAllFutureBookingsOfUser(userId, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Page<Booking> findAllFutureBookingsOfUser(Long userId, Pageable pageable) {
        return bookingRepository.findAllFutureBookingsOfUser(userId, LocalDateTime.now(), pageable);
    }

    @Override
    public Collection<Booking> findAllPastBookingsOfUser(Long userId) {
        return bookingRepository.findAllPreviousBookingsOfUser(userId, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Page<Booking> findAllPastBookingsOfUser(Long userId, Pageable pageable) {
        return bookingRepository.findAllPreviousBookingsOfUser(userId, LocalDateTime.now(), pageable);
    }

    @Override
    public Collection<Booking> findAllCurrentBookingsOfUser(Long userId) {
        return bookingRepository.findAllCurrentBookingsOfUser(userId, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Page<Booking> findAllCurrentBookingsOfUser(Long userId, Pageable pageable) {
        return bookingRepository.findAllCurrentBookingsOfUser(userId, LocalDateTime.now(), pageable);
    }

    @Override
    public Collection<Booking> findAllBookingOfUser(Long userId) {
        return bookingRepository.findAllBookingsOfUser(userId, bookingSort);
    }

    @Override
    public Page<Booking> findAllBookingOfUser(Long userId, Pageable pageable) {
        return bookingRepository.findAllBookingsOfUser(userId, pageable);
    }

    @Override
    public Collection<Booking> findAllBookingsOfItemsWithStatus(Collection<Long> itemIds, BookingStatus status) {
        return bookingRepository.findAllBookingsOfItemsByStatus(itemIds, status, bookingSort);
    }

    @Override
    public Page<Booking> findAllBookingsOfItemsWithStatus(Collection<Long> itemIds, BookingStatus status, Pageable pageable) {
        return bookingRepository.findAllBookingsOfItemsByStatus(itemIds, status, pageable);
    }

    @Override
    public Collection<Booking> findAllFutureBookingsOfItems(Collection<Long> itemIds) {
        return bookingRepository.findAllFutureBookingsOfItems(itemIds, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Page<Booking> findAllFutureBookingsOfItems(Collection<Long> itemIds, Pageable pageable) {
        return bookingRepository.findAllFutureBookingsOfItems(itemIds, LocalDateTime.now(), pageable);
    }

    @Override
    public Collection<Booking> findAllPastBookingsOfItems(Collection<Long> itemIds) {
        return bookingRepository.findAllPreviousBookingsOfItems(itemIds, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Page<Booking> findAllPastBookingsOfItems(Collection<Long> itemIds, Pageable pageable) {
        return bookingRepository.findAllPreviousBookingsOfItems(itemIds, LocalDateTime.now(), pageable);
    }

    @Override
    public Collection<Booking> findAllCurrentBookingsOfItems(Collection<Long> itemIds) {
        return bookingRepository.findAllCurrentBookingsOfItems(itemIds, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Page<Booking> findAllCurrentBookingsOfItems(Collection<Long> itemIds, Pageable pageable) {
        return bookingRepository.findAllCurrentBookingsOfItems(itemIds, LocalDateTime.now(), pageable);
    }

    @Override
    public Collection<Booking> findAllBookingOfItems(Collection<Long> itemIds) {
        return bookingRepository.findAllBookingsOfItems(itemIds, bookingSort);
    }

    @Override
    public Page<Booking> findAllBookingOfItems(Collection<Long> itemIds, Pageable pageable) {
        return bookingRepository.findAllBookingsOfItems(itemIds, pageable);
    }

    @Override
    public Boolean isBookingOverlapsOthers(Booking booking) {
        Integer overlapsCount = bookingRepository.getOverlapsBookingsCount(
                booking.getItem().getId(),
                booking.getStart(),
                booking.getEnd()
        );
        return overlapsCount > 0;
    }
}
