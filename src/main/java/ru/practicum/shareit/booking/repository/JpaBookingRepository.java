package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;
import java.util.Collection;

public interface JpaBookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.end < :curTime")
    Collection<Booking> findAllPreviousBookingsOfUser(Long userId, LocalDateTime curTime, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.end < :curTime")
    Page<Booking> findAllPreviousBookingsOfUser(Long userId, LocalDateTime curTime, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.start > :curTime")
    Collection<Booking> findAllFutureBookingsOfUser(Long userId, LocalDateTime curTime, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.start > :curTime")
    Page<Booking> findAllFutureBookingsOfUser(Long userId, LocalDateTime curTime, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND :curTime BETWEEN b.start AND b.end")
    Collection<Booking> findAllCurrentBookingsOfUser(Long userId, LocalDateTime curTime, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND :curTime BETWEEN b.start AND b.end")
    Page<Booking> findAllCurrentBookingsOfUser(Long userId, LocalDateTime curTime, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.status = :status")
    Collection<Booking> findAllBookingsOfUserByStatus(Long userId, BookingStatus status, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.status = :status")
    Page<Booking> findAllBookingsOfUserByStatus(Long userId, BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId")
    Collection<Booking> findAllBookingsOfUser(Long userId, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId")
    Page<Booking> findAllBookingsOfUser(Long userId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND b.end < :curTime")
    Collection<Booking> findAllPreviousBookingsOfItems(Collection<Long> itemIds, LocalDateTime curTime, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND b.end < :curTime")
    Page<Booking> findAllPreviousBookingsOfItems(Collection<Long> itemIds, LocalDateTime curTime, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND b.start > :curTime")
    Collection<Booking> findAllFutureBookingsOfItems(Collection<Long> itemIds, LocalDateTime curTime, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND b.start > :curTime")
    Page<Booking> findAllFutureBookingsOfItems(Collection<Long> itemIds, LocalDateTime curTime, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND :curTime BETWEEN b.start AND b.end")
    Collection<Booking> findAllCurrentBookingsOfItems(Collection<Long> itemIds, LocalDateTime curTime, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND :curTime BETWEEN b.start AND b.end")
    Page<Booking> findAllCurrentBookingsOfItems(Collection<Long> itemIds, LocalDateTime curTime, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND b.status = :status")
    Collection<Booking> findAllBookingsOfItemsByStatus(Collection<Long> itemIds, BookingStatus status, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds AND b.status = :status")
    Page<Booking> findAllBookingsOfItemsByStatus(Collection<Long> itemIds, BookingStatus status, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds")
    Collection<Booking> findAllBookingsOfItems(Collection<Long> itemIds, Sort sort);

    @Query("SELECT b FROM Booking b WHERE b.item.id IN :itemIds")
    Page<Booking> findAllBookingsOfItems(Collection<Long> itemIds, Pageable pageable);

    @Query("SELECT count(b) FROM Booking b " +
            "WHERE b.item.id = :itemId AND b.status <> 'REJECTED' AND :start < b.end AND :end >= b.start")
    Integer getOverlapsBookingsCount(Long itemId, LocalDateTime start, LocalDateTime end);

    Booking findFirstByItem_IdAndStartIsAfterAndStatusOrderByStart(Long itemId, LocalDateTime curTime, BookingStatus status);

    Booking findFirstByItem_IdAndStartIsBeforeAndStatusOrderByEndDesc(Long itemId, LocalDateTime curTime, BookingStatus status);

    Integer countByItem_IdAndBooker_IdAndStatusAndEndIsBefore(Long itemId, Long bookerId, BookingStatus status, LocalDateTime curTime);
}
