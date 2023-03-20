package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.mapper.BookingMapper;
import ru.practicum.shareit.booking.dto.mapper.BookingRequestMapper;
import ru.practicum.shareit.booking.exceptions.BookingNotFoundException;
import ru.practicum.shareit.booking.exceptions.BookingOverlapsException;
import ru.practicum.shareit.booking.exceptions.BookingUpdateException;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.DatabaseBookingRepositoryImpl;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.booking.service.provider.BookingProviderSelector;
import ru.practicum.shareit.config.BaseConfig;
import ru.practicum.shareit.config.PageBuilder;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.ItemMapper;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.ItemUnavailableException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DatabaseItemRepositoryImpl;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.exceptions.InvalidPaginationParamsException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.mapper.UserMapper;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.DatabaseUserRepositoryImpl;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest(
        classes = {
                BookingServiceImpl.class,
                BookingProviderSelector.class,
                BookingRequestMapper.class,
                UserMapper.class,
                ItemMapper.class,
                BaseConfig.class,
                BookingMapper.class,
                PageBuilder.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@MockBean(classes = {
        DatabaseBookingRepositoryImpl.class,
        DatabaseItemRepositoryImpl.class,
        DatabaseUserRepositoryImpl.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {

    private final BookingRepository bookingRepository;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final BookingService bookingService;

    private final Long expectedBookingId = 1L;

    private final Long expectedItemId = 2L;

    private final Long expectedUserId = 3L;

    private final ItemDto itemDto = new ItemDto(
            expectedItemId,
            "test_item_name",
            "test_description",
            Boolean.TRUE,
            null,
            null,
            null,
            Collections.emptyList()
    );

    private final UserDto userDto = new UserDto(
            expectedUserId,
            "test_name",
            "test_email@test.domain.com"
    );

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto(
            expectedItemId,
            LocalDateTime.of(2043, 1, 1, 0, 0, 0),
            LocalDateTime.of(2043, 1, 1, 3, 0, 0)
    );

    private final BookingResponseDto bookingResponseDto = new BookingResponseDto(
            expectedBookingId,
            bookingRequestDto.getStart(),
            bookingRequestDto.getEnd(),
            BookingStatus.WAITING,
            itemDto,
            userDto
    );

    private final User user = new User(
            userDto.getName(),
            userDto.getEmail()
    ).withId(expectedUserId);

    private final Item item = new Item(
            itemDto.getName(),
            itemDto.getDescription(),
            itemDto.getAvailable(),
            null,
            null
    ).withId(expectedItemId);

    private final Booking booking = new Booking(
            bookingRequestDto.getStart(),
            bookingRequestDto.getEnd(),
            null,
            null,
            BookingStatus.WAITING
    ).withId(expectedBookingId);

    @Test
    @DisplayName("Create valid booking test")
    void createValidBookingTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findItemById(anyLong())).thenReturn(item);
        when(bookingRepository.isBookingOverlapsOthers(any())).thenReturn(Boolean.FALSE);
        when(bookingRepository.saveBooking(any())).thenReturn(booking.withBooker(user).withItem(item));
        BookingResponseDto result = bookingService.createBooking(expectedUserId, bookingRequestDto);
        assertThat(result).isEqualTo(bookingResponseDto);
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(bookingRepository, times(1)).isBookingOverlapsOthers(any());
        verify(bookingRepository, times(1)).saveBooking(any());
    }

    @Test
    @DisplayName("Create booking by not existing user test")
    void createBookingByNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> bookingService.createBooking(expectedUserId, bookingRequestDto));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).findItemById(anyLong());
        verify(bookingRepository, never()).isBookingOverlapsOthers(any());
        verify(bookingRepository, never()).saveBooking(any());
    }

    @Test
    @DisplayName("Create booking of not existing item test")
    void createBookingOfNotExistingItemTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findItemById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(ItemNotFoundException.class)
                .isThrownBy(() -> bookingService.createBooking(expectedUserId, bookingRequestDto));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(bookingRepository, never()).isBookingOverlapsOthers(any());
        verify(bookingRepository, never()).saveBooking(any());
    }

    @Test
    @DisplayName("Create booking of not available item test")
    void createBookingOfNotAvailableItemTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findItemById(anyLong())).thenReturn(item.withAvailable(Boolean.FALSE));
        assertThatExceptionOfType(ItemUnavailableException.class)
                .isThrownBy(() -> bookingService.createBooking(expectedUserId, bookingRequestDto));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(bookingRepository, never()).isBookingOverlapsOthers(any());
        verify(bookingRepository, never()).saveBooking(any());
    }

    @Test
    @DisplayName("Create booking for own item test")
    void createBookingForOwnItemTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findItemById(anyLong())).thenReturn(item.withOwner(user));
        assertThatExceptionOfType(ItemNotFoundException.class)
                .isThrownBy(() -> bookingService.createBooking(expectedUserId, bookingRequestDto));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(bookingRepository, never()).isBookingOverlapsOthers(any());
        verify(bookingRepository, never()).saveBooking(any());
    }

    @Test
    @DisplayName("Create booking which overlaps others test")
    void createBookingWhichOverlapsOthersTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findItemById(anyLong())).thenReturn(item);
        when(bookingRepository.isBookingOverlapsOthers(any())).thenReturn(Boolean.TRUE);
        assertThatExceptionOfType(BookingOverlapsException.class)
                .isThrownBy(() -> bookingService.createBooking(expectedUserId, bookingRequestDto));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(bookingRepository, times(1)).isBookingOverlapsOthers(any());
        verify(bookingRepository, never()).saveBooking(any());
    }

    @Test
    @DisplayName("Update existing booking by item owner with approving test")
    void updateExistingBookingByItemOwnerWithApprovingTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findBookingById(anyLong())).thenReturn(booking.withItem(item.withOwner(user)));
        when(bookingRepository.isBookingOverlapsOthers(any())).thenReturn(Boolean.FALSE);
        when(bookingRepository.saveBooking(any())).thenReturn(booking.withBooker(user).withItem(item));
        BookingResponseDto result = bookingService.updateBooking(expectedUserId, expectedBookingId, Boolean.TRUE);
        assertThat(result).isEqualTo(bookingResponseDto);
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, times(1)).findBookingById(anyLong());
        verify(bookingRepository, never()).isBookingOverlapsOthers(any());
        verify(bookingRepository, times(1)).saveBooking(any());
    }

    @Test
    @DisplayName("Update existing booking by item owner with rejecting test")
    void updateExistingBookingByItemOwnerWithRejectingTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findBookingById(anyLong())).thenReturn(booking.withItem(item.withOwner(user)));
        when(bookingRepository.isBookingOverlapsOthers(any())).thenReturn(Boolean.FALSE);
        when(bookingRepository.saveBooking(any()))
                .thenReturn(booking.withBooker(user).withItem(item).withStatus(BookingStatus.REJECTED));
        BookingResponseDto result = bookingService.updateBooking(expectedUserId, expectedBookingId, Boolean.FALSE);
        assertThat(result).isEqualTo(bookingResponseDto.withStatus(BookingStatus.REJECTED));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, times(1)).findBookingById(anyLong());
        verify(bookingRepository, never()).isBookingOverlapsOthers(any());
        verify(bookingRepository, times(1)).saveBooking(any());
    }

    @Test
    @DisplayName("Update booking by not existing owner test")
    void updateBookingByNotExistingOwnerTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> bookingService.updateBooking(expectedUserId, expectedBookingId, Boolean.TRUE));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, never()).findBookingById(anyLong());
        verify(bookingRepository, never()).isBookingOverlapsOthers(any());
        verify(bookingRepository, never()).saveBooking(any());
    }

    @Test
    @DisplayName("Update not existing booking test")
    void updateNotExistingBookingTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findBookingById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(BookingNotFoundException.class)
                .isThrownBy(() -> bookingService.updateBooking(expectedUserId, expectedBookingId, Boolean.TRUE));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, times(1)).findBookingById(anyLong());
        verify(bookingRepository, never()).isBookingOverlapsOthers(any());
        verify(bookingRepository, never()).saveBooking(any());
    }

    @Test
    @DisplayName("Update booking by wrong owner test")
    void updateBookingByWrongOwnerTest() throws Exception {
        Long wrongOwnerId = 99L;
        when(userRepository.findUserById(anyLong())).thenReturn(user.withId(wrongOwnerId));
        when(bookingRepository.findBookingById(anyLong())).thenReturn(booking.withItem(item.withOwner(user)));
        assertThatExceptionOfType(BookingNotFoundException.class)
                .isThrownBy(() -> bookingService.updateBooking(expectedUserId, expectedBookingId, Boolean.TRUE));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, times(1)).findBookingById(anyLong());
        verify(bookingRepository, never()).isBookingOverlapsOthers(any());
        verify(bookingRepository, never()).saveBooking(any());
    }

    @Test
    @DisplayName("Update already approved booking test")
    void updateAlreadyApprovedBookingTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findBookingById(anyLong()))
                .thenReturn(booking.withItem(item.withOwner(user)).withStatus(BookingStatus.APPROVED));
        assertThatExceptionOfType(BookingUpdateException.class)
                .isThrownBy(() -> bookingService.updateBooking(expectedUserId, expectedBookingId, Boolean.TRUE));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, times(1)).findBookingById(anyLong());
        verify(bookingRepository, never()).isBookingOverlapsOthers(any());
        verify(bookingRepository, never()).saveBooking(any());
    }

    @Test
    @DisplayName("Get existing booking by item owner test")
    void getExistingBookingByItemOwnerTest() throws Exception {
        Long expectedBookerId = 99L;
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findBookingById(anyLong()))
                .thenReturn(booking.withBooker(user.withId(expectedBookerId)).withItem(item.withOwner(user)));
        BookingResponseDto result = bookingService.getBooking(expectedUserId, expectedBookingId);
        assertThat(result).isEqualTo(bookingResponseDto.withBookerDto(userDto.withId(expectedBookerId)));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, times(1)).findBookingById(anyLong());
    }

    @Test
    @DisplayName("Get existing booking by booker test")
    void getExistingBookingByBookerTest() throws Exception {
        Long expectedOwnerId = 99L;
        when(userRepository.findUserById(anyLong())).thenReturn(user.withId(expectedOwnerId));
        when(bookingRepository.findBookingById(anyLong()))
                .thenReturn(booking.withItem(item.withOwner(user.withId(expectedOwnerId))).withBooker(user));
        BookingResponseDto result = bookingService.getBooking(expectedOwnerId, expectedBookingId);
        assertThat(result).isEqualTo(bookingResponseDto);
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, times(1)).findBookingById(anyLong());
    }

    @Test
    @DisplayName("Get booking by not existing user test")
    void getBookingByNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> bookingService.getBooking(expectedUserId, expectedBookingId));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, never()).findBookingById(anyLong());
    }

    @Test
    @DisplayName("Get not existing booking test")
    void getNotExistingBookingTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findBookingById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(BookingNotFoundException.class)
                .isThrownBy(() -> bookingService.getBooking(expectedUserId, expectedBookingId));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, times(1)).findBookingById(anyLong());
    }

    @Test
    @DisplayName("Get booking by other user test")
    void getBookingByOtherUserTest() throws Exception {
        Long wrongUserId = 99L;
        Long bookerId = 100L;
        when(userRepository.findUserById(anyLong())).thenReturn(user.withId(wrongUserId));
        when(bookingRepository.findBookingById(anyLong()))
                .thenReturn(booking.withItem(item.withOwner(user)).withBooker(user.withId(bookerId)));
        assertThatExceptionOfType(BookingNotFoundException.class)
                .isThrownBy(() -> bookingService.getBooking(expectedUserId, expectedBookingId));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, times(1)).findBookingById(anyLong());
    }

    @ParameterizedTest
    @DisplayName("Get all bookings of user without page test")
    @ValueSource(strings = {"ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED"})
    void getAllBookingsOfUserWithoutPageTest(String state) throws Exception {
        when(bookingRepository.findAllBookingOfUser(expectedUserId))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(bookingRepository.findAllBookingsOfUserWithStatus(expectedUserId, BookingStatus.WAITING))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(bookingRepository.findAllFutureBookingsOfUser(expectedUserId))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(bookingRepository.findAllCurrentBookingsOfUser(expectedUserId))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(bookingRepository.findAllPastBookingsOfUser(expectedUserId))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(bookingRepository.findAllBookingsOfUserWithStatus(expectedUserId, BookingStatus.REJECTED))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        Collection<BookingResponseDto> result = bookingService
                .getAllBookingsOfUser(expectedUserId, state, null, null);
        assertThat(result).asList().isNotEmpty().contains(bookingResponseDto);
        verify(userRepository, times(1)).findUserById(anyLong());
        if (state.equals("ALL")) {
            verify(bookingRepository, times(1)).findAllBookingOfUser(anyLong());
        }
        if (state.equals("CURRENT")) {
            verify(bookingRepository, times(1)).findAllCurrentBookingsOfUser(anyLong());
        }
        if (state.equals("PAST")) {
            verify(bookingRepository, times(1)).findAllPastBookingsOfUser(anyLong());
        }
        if (state.equals("FUTURE")) {
            verify(bookingRepository, times(1)).findAllFutureBookingsOfUser(anyLong());
        }
        if (state.equals("WAITING")) {
            verify(bookingRepository, times(1)).findAllBookingsOfUserWithStatus(anyLong(), any());
        }
        if (state.equals("REJECTED")) {
            verify(bookingRepository, times(1)).findAllBookingsOfUserWithStatus(anyLong(), any());
        }
    }

    @ParameterizedTest
    @DisplayName("Get all bookings of user with page test")
    @ValueSource(strings = {"ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED"})
    void getAllBookingsOfUserWithPageTest(String state) throws Exception {
        Integer from = 0;
        Integer size = 1;
        when(bookingRepository.findAllBookingOfUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(bookingRepository.findAllBookingsOfUserWithStatus(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(bookingRepository.findAllFutureBookingsOfUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(bookingRepository.findAllCurrentBookingsOfUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(bookingRepository.findAllPastBookingsOfUser(anyLong(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(bookingRepository.findAllBookingsOfUserWithStatus(anyLong(), any(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        Collection<BookingResponseDto> result = bookingService
                .getAllBookingsOfUser(expectedUserId, state, from, size);
        assertThat(result).asList().isNotEmpty().contains(bookingResponseDto);
        verify(userRepository, times(1)).findUserById(anyLong());
        if (state.equals("ALL")) {
            verify(bookingRepository, times(1))
                    .findAllBookingOfUser(anyLong(), any());
        }
        if (state.equals("CURRENT")) {
            verify(bookingRepository, times(1))
                    .findAllCurrentBookingsOfUser(anyLong(), any());
        }
        if (state.equals("PAST")) {
            verify(bookingRepository, times(1))
                    .findAllPastBookingsOfUser(anyLong(), any());
        }
        if (state.equals("FUTURE")) {
            verify(bookingRepository, times(1))
                    .findAllFutureBookingsOfUser(anyLong(), any());
        }
        if (state.equals("WAITING")) {
            verify(bookingRepository, times(1))
                    .findAllBookingsOfUserWithStatus(anyLong(), any(), any());
        }
        if (state.equals("REJECTED")) {
            verify(bookingRepository, times(1))
                    .findAllBookingsOfUserWithStatus(anyLong(), any(), any());
        }
    }

    @Test
    @DisplayName("Get all bookings of not existing user test")
    void getAllBookingsOfNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> bookingService.getAllBookingsOfUser(expectedUserId, "", null, null));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, never()).findAllBookingOfUser(anyLong());
        verify(bookingRepository, never()).findAllCurrentBookingsOfUser(anyLong());
        verify(bookingRepository, never()).findAllPastBookingsOfUser(anyLong());
        verify(bookingRepository, never()).findAllFutureBookingsOfUser(anyLong());
        verify(bookingRepository, never()).findAllBookingsOfUserWithStatus(anyLong(), any());
        verify(bookingRepository, never()).findAllBookingsOfUserWithStatus(anyLong(), any());
    }

    @Test
    @DisplayName("Get all bookings of user with wrong page test")
    void getAllBookingsOfUserWithWrongPageTest() throws Exception {
        Integer from = -1;
        Integer size = 0;
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        assertThatExceptionOfType(InvalidPaginationParamsException.class)
                .isThrownBy(() -> bookingService.getAllBookingsOfUser(expectedUserId, "ALL", from, size));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(bookingRepository, never()).findAllBookingOfUser(anyLong(), any());
        verify(bookingRepository, never()).findAllCurrentBookingsOfUser(anyLong(), any());
        verify(bookingRepository, never()).findAllPastBookingsOfUser(anyLong(), any());
        verify(bookingRepository, never()).findAllFutureBookingsOfUser(anyLong(), any());
        verify(bookingRepository, never()).findAllBookingsOfUserWithStatus(anyLong(), any(), any());
        verify(bookingRepository, never()).findAllBookingsOfUserWithStatus(anyLong(), any(), any());
    }

    @ParameterizedTest
    @DisplayName("Get all bookings of item owner without page test")
    @ValueSource(strings = {"ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED"})
    void getAllBookingsOfItemOwnerWithoutPageTest(String state) throws Exception {
        when(bookingRepository.findAllBookingOfItems(any()))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(bookingRepository.findAllBookingsOfItemsWithStatus(any(), any()))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(bookingRepository.findAllFutureBookingsOfItems(any()))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(bookingRepository.findAllCurrentBookingsOfItems(any()))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(bookingRepository.findAllPastBookingsOfItems(any()))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(bookingRepository.findAllBookingsOfItemsWithStatus(any(), any()))
                .thenReturn(Lists.list(booking.withBooker(user).withItem(item)));
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findAllItems(anyLong())).thenReturn(List.of(item));
        Collection<BookingResponseDto> result = bookingService
                .getAllBookingsForOwnerItems(expectedUserId, state, null, null);
        assertThat(result).asList().isNotEmpty().contains(bookingResponseDto);
        verify(itemRepository, times(1)).findAllItems(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        if (state.equals("ALL")) {
            verify(bookingRepository, times(1)).findAllBookingOfItems(any());
        }
        if (state.equals("CURRENT")) {
            verify(bookingRepository, times(1)).findAllCurrentBookingsOfItems(any());
        }
        if (state.equals("PAST")) {
            verify(bookingRepository, times(1)).findAllPastBookingsOfItems(any());
        }
        if (state.equals("FUTURE")) {
            verify(bookingRepository, times(1)).findAllFutureBookingsOfItems(any());
        }
        if (state.equals("WAITING")) {
            verify(bookingRepository, times(1)).findAllBookingsOfItemsWithStatus(any(), any());
        }
        if (state.equals("REJECTED")) {
            verify(bookingRepository, times(1)).findAllBookingsOfItemsWithStatus(any(), any());
        }
    }

    @ParameterizedTest
    @DisplayName("Get all bookings of item owner with page test")
    @ValueSource(strings = {"ALL", "CURRENT", "PAST", "FUTURE", "WAITING", "REJECTED"})
    void getAllBookingsOfItemOwnerWithPageTest(String state) throws Exception {
        Integer from = 0;
        Integer size = 1;
        when(bookingRepository.findAllBookingOfItems(any(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(bookingRepository.findAllBookingsOfItemsWithStatus(any(), any(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(bookingRepository.findAllFutureBookingsOfItems(any(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(bookingRepository.findAllCurrentBookingsOfItems(any(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(bookingRepository.findAllPastBookingsOfItems(any(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(bookingRepository.findAllBookingsOfItemsWithStatus(any(), any(), any()))
                .thenReturn(new PageImpl<>(Lists.list(booking.withBooker(user).withItem(item))));
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findAllItems(anyLong())).thenReturn(List.of(item));
        Collection<BookingResponseDto> result = bookingService
                .getAllBookingsForOwnerItems(expectedUserId, state, from, size);
        assertThat(result).asList().isNotEmpty().contains(bookingResponseDto);
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findAllItems(anyLong());
        if (state.equals("ALL")) {
            verify(bookingRepository, times(1))
                    .findAllBookingOfItems(any(), any());
        }
        if (state.equals("CURRENT")) {
            verify(bookingRepository, times(1))
                    .findAllCurrentBookingsOfItems(any(), any());
        }
        if (state.equals("PAST")) {
            verify(bookingRepository, times(1))
                    .findAllPastBookingsOfItems(any(), any());
        }
        if (state.equals("FUTURE")) {
            verify(bookingRepository, times(1))
                    .findAllFutureBookingsOfItems(any(), any());
        }
        if (state.equals("WAITING")) {
            verify(bookingRepository, times(1))
                    .findAllBookingsOfItemsWithStatus(any(), any(), any());
        }
        if (state.equals("REJECTED")) {
            verify(bookingRepository, times(1))
                    .findAllBookingsOfItemsWithStatus(any(), any(), any());
        }
    }

    @Test
    @DisplayName("Get all bookings of not existing item owner test")
    void getAllBookingsOfNotExistingItemOwnerTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> bookingService.getAllBookingsForOwnerItems(expectedUserId, "", null, null));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).findAllItems(anyLong());
        verify(bookingRepository, never()).findAllBookingOfItems(any());
        verify(bookingRepository, never()).findAllCurrentBookingsOfItems(any());
        verify(bookingRepository, never()).findAllPastBookingsOfItems(any());
        verify(bookingRepository, never()).findAllFutureBookingsOfItems(any());
        verify(bookingRepository, never()).findAllBookingsOfItemsWithStatus(any(), any());
        verify(bookingRepository, never()).findAllBookingsOfItemsWithStatus(any(), any());
    }

    @Test
    @DisplayName("Get all bookings of item owner with wrong page test")
    void getAllBookingsOfItemOwnerWithWrongPageTest() throws Exception {
        Integer from = -1;
        Integer size = 0;
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findAllItems(anyLong())).thenReturn(List.of(item));
        assertThatExceptionOfType(InvalidPaginationParamsException.class)
                .isThrownBy(() -> bookingService.getAllBookingsForOwnerItems(expectedUserId, "ALL", from, size));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findAllItems(anyLong());
        verify(bookingRepository, never()).findAllBookingOfItems(any(), any());
        verify(bookingRepository, never()).findAllCurrentBookingsOfItems(any(), any());
        verify(bookingRepository, never()).findAllPastBookingsOfItems(any(), any());
        verify(bookingRepository, never()).findAllFutureBookingsOfItems(any(), any());
        verify(bookingRepository, never()).findAllBookingsOfItemsWithStatus(any(), any(), any());
        verify(bookingRepository, never()).findAllBookingsOfItemsWithStatus(any(), any(), any());
    }
}
