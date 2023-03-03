package ru.practicum.shareit.booking.repository;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

@Repository
@Transactional(readOnly = true)
public class DatabaseBookingRepositoryImpl implements BookingRepository {

    private final JpaBookingRepository bookingRepository;
    private final Sort bookingSort = Sort.by(Sort.Direction.DESC, "start");

    public DatabaseBookingRepositoryImpl(@Lazy JpaBookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

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
    public Collection<Booking> findAllFutureBookingsOfUser(Long userId) {
        return bookingRepository.findAllFutureBookingsOfUser(userId, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Collection<Booking> findAllPastBookingsOfUser(Long userId) {
        return bookingRepository.findAllPreviousBookingsOfUser(userId, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Collection<Booking> findAllCurrentBookingsOfUser(Long userId) {
        return bookingRepository.findAllCurrentBookingsOfUser(userId, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Collection<Booking> findAllBookingOfUser(Long userId) {
        return bookingRepository.findAllBookingsOfUser(userId, bookingSort);
    }

    @Override
    public Collection<Booking> findAllBookingsOfItemsWithStatus(Collection<Long> itemIds, BookingStatus status) {
        return bookingRepository.findAllBookingsOfItemsByStatus(itemIds, status, bookingSort);
    }

    @Override
    public Collection<Booking> findAllFutureBookingsOfItems(Collection<Long> itemIds) {
        return bookingRepository.findAllFutureBookingsOfItems(itemIds, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Collection<Booking> findAllPastBookingsOfItems(Collection<Long> itemIds) {
        return bookingRepository.findAllPreviousBookingsOfItems(itemIds, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Collection<Booking> findAllCurrentBookingsOfItems(Collection<Long> itemIds) {
        return bookingRepository.findAllCurrentBookingsOfItems(itemIds, LocalDateTime.now(), bookingSort);
    }

    @Override
    public Collection<Booking> findAllBookingOfItems(Collection<Long> itemIds) {
        return bookingRepository.findAllBookingsOfItems(itemIds, bookingSort);
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