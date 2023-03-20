package ru.practicum.shareit.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.DatabaseBookingRepositoryImpl;
import ru.practicum.shareit.config.IdReducer;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Rollback
@DataJpaTest
@Transactional(readOnly = true)
@Sql(scripts = "classpath:booking_init.sql")
@Import(value = {DatabaseBookingRepositoryImpl.class, IdReducer.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingRepositoryTest {

    private final BookingRepository bookingRepository;

    private final IdReducer idReducer;

    private final Long expectedBookerId = 2L;

    private final Long expectedItemId = 1L;

    private final Booking booking = new Booking(
            LocalDateTime.of(2043, 1, 1, 16, 0, 0),
            LocalDateTime.of(2043, 1, 1, 17, 0, 0),
            new Item().withId(expectedItemId),
            new User().withId(expectedBookerId),
            BookingStatus.WAITING
    );

    @BeforeEach
    public void setUp() throws SQLException {
        idReducer.resetAutoIncrementColumns("items");
        idReducer.resetAutoIncrementColumns("users");
        idReducer.resetAutoIncrementColumns("bookings");
    }

    @Test
    @DisplayName("Find all past bookings of user test")
    void findAllPastBookingsOfUserTest() throws Exception {
        assertThat(bookingRepository.findAllPastBookingsOfUser(expectedBookerId)).asList().hasSize(1);
    }

    @Test
    @DisplayName("Find all current bookings of user test")
    void findAllCurrentBookingsOfUserTest() throws Exception {
        assertThat(bookingRepository.findAllCurrentBookingsOfUser(expectedBookerId)).asList().hasSize(1);
    }

    @Test
    @DisplayName("Find all future bookings of user test")
    void findAllFutureBookingsOfUserTest() throws Exception {
        assertThat(bookingRepository.findAllFutureBookingsOfUser(expectedBookerId)).asList().hasSize(3);
    }

    @Test
    @DisplayName("Find all bookings of user test")
    void findAllBookingsOfUserTest() throws Exception {
        assertThat(bookingRepository.findAllBookingOfUser(expectedBookerId)).asList().hasSize(5);
    }

    @Test
    @DisplayName("Find all rejected bookings of user test")
    void findAllRejectedBookingsOfUserTest() throws Exception {
        assertThat(bookingRepository.findAllBookingsOfUserWithStatus(expectedBookerId, BookingStatus.REJECTED))
                .asList().hasSize(1);
    }

    @Test
    @DisplayName("Find all waiting bookings of user test")
    void findAllWaitingBookingsOfUserTest() throws Exception {
        assertThat(bookingRepository.findAllBookingsOfUserWithStatus(expectedBookerId, BookingStatus.WAITING))
                .asList().hasSize(1);
    }

    @Test
    @DisplayName("Find all past bookings of item test")
    void findAllPastBookingsOfItemTest() throws Exception {
        assertThat(bookingRepository.findAllPastBookingsOfItems(List.of(expectedItemId))).asList().hasSize(1);
    }

    @Test
    @DisplayName("Find all current bookings of item test")
    void findAllCurrentBookingsOfItemTest() throws Exception {
        assertThat(bookingRepository.findAllCurrentBookingsOfItems(List.of(expectedItemId))).asList().hasSize(1);
    }

    @Test
    @DisplayName("Find all future bookings of item test")
    void findAllFutureBookingsOfItemTest() throws Exception {
        assertThat(bookingRepository.findAllFutureBookingsOfItems(List.of(expectedItemId))).asList().hasSize(3);
    }

    @Test
    @DisplayName("Find all bookings of item test")
    void findAllBookingsOfItemTest() throws Exception {
        assertThat(bookingRepository.findAllBookingOfItems(List.of(expectedItemId))).asList().hasSize(5);
    }

    @Test
    @DisplayName("Find all rejected bookings of item test")
    void findAllRejectedBookingsOfItemTest() throws Exception {
        assertThat(bookingRepository.findAllBookingsOfItemsWithStatus(List.of(expectedItemId), BookingStatus.REJECTED))
                .asList().hasSize(1);
    }

    @Test
    @DisplayName("Find all waiting bookings of item test")
    void findAllWaitingBookingsOfItemTest() throws Exception {
        assertThat(bookingRepository.findAllBookingsOfItemsWithStatus(List.of(expectedItemId), BookingStatus.WAITING))
                .asList().hasSize(1);
    }

    @Test
    @DisplayName("Is booking overlaps others false test")
    void IsBookingOverlapsOthersFalseTest() throws Exception {
        assertThat(bookingRepository.isBookingOverlapsOthers(booking)).isFalse();
    }

    @Test
    @DisplayName("Is booking overlaps others true test")
    void IsBookingOverlapsOthersTrueTest() throws Exception {
        Booking overlapsBooking = booking.withStart(LocalDateTime.of(2043, 1, 1, 10, 0, 0));
        assertThat(bookingRepository.isBookingOverlapsOthers(overlapsBooking)).isTrue();
    }
}
