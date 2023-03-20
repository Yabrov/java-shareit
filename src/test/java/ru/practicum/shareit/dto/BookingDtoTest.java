package ru.practicum.shareit.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingLinkedDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.mapper.BookingLinkedMapper;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.dto.mapper.BookingRequestMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {
        BookingMapper.class,
        BookingRequestMapper.class,
        BookingLinkedMapper.class,
        UserMapper.class,
        ItemMapper.class,
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {

    private final Converter<Booking, BookingResponseDto> bookingMapper;

    private final Converter<BookingRequestDto, Booking> bookingRequestMapper;

    private final Converter<Booking, BookingLinkedDto> bookingLinkedDtoMapper;

    private final User user = new User(
            "test_name",
            "test_email"
    ).withId(1L);

    private final UserDto userDto = new UserDto(
            1L,
            "test_name",
            "test_email"
    );

    private final Item item = new Item(
            "name",
            "description",
            true,
            user,
            null
    ).withId(1L);

    private final ItemDto itemDto = new ItemDto(
            1L,
            "name",
            "description",
            true,
            null,
            null,
            null,
            Collections.emptyList()
    );

    private final Booking booking = new Booking(
            LocalDateTime.of(2043, 1, 1, 0, 0, 0),
            LocalDateTime.of(2043, 1, 1, 3, 0, 0),
            item,
            user,
            BookingStatus.WAITING
    ).withId(1L);

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto(
            item.getId(),
            LocalDateTime.of(2043, 1, 1, 0, 0, 0),
            LocalDateTime.of(2043, 1, 1, 3, 0, 0)
    );

    private final BookingResponseDto bookingResponseDto = new BookingResponseDto(
            1L,
            LocalDateTime.of(2043, 1, 1, 0, 0, 0),
            LocalDateTime.of(2043, 1, 1, 3, 0, 0),
            BookingStatus.WAITING,
            itemDto,
            userDto
    );

    private final BookingLinkedDto bookingLinkedDto = new BookingLinkedDto(1L, user.getId());

    @Test
    @DisplayName("Booking entity mapper test")
    void bookingEntityMapperTest() throws Exception {
        assertThat(bookingMapper.convert(booking)).isEqualTo(bookingResponseDto);
    }

    @Test
    @DisplayName("Booking request dto mapper test")
    void bookingRequestDtoMapperTest() throws Exception {
        assertThat(bookingRequestMapper.convert(bookingRequestDto).withId(1L)).isEqualTo(booking);
    }

    @Test
    @DisplayName("Booking to linked dto mapper test")
    void bookingToLinkedDtoMapperTest() throws Exception {
        assertThat(bookingLinkedDtoMapper.convert(booking)).isEqualTo(bookingLinkedDto);
    }
}
