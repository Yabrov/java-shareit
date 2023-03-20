package ru.practicum.shareit.service;

import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingLinkedDto;
import ru.practicum.shareit.booking.dto.mapper.BookingLinkedMapper;
import ru.practicum.shareit.config.PageBuilder;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.mapper.*;
import ru.practicum.shareit.item.exceptions.CommentCreateException;
import ru.practicum.shareit.item.exceptions.ItemNotFoundException;
import ru.practicum.shareit.item.exceptions.WrongItemOwnerException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.DatabaseItemRepositoryImpl;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.request.exceptions.InvalidPaginationParamsException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.repository.DatabaseUserRepositoryImpl;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(
        classes = {
                ItemServiceImpl.class,
                BookingLinkedMapper.class,
                CommentDtoMapper.class,
                CommentMapper.class,
                ItemDtoMapper.class,
                ItemMapper.class,
                ItemToSimpleDtoMapper.class,
                PageBuilder.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@MockBean(classes = {
        DatabaseItemRepositoryImpl.class,
        DatabaseUserRepositoryImpl.class
})
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final ItemService itemService;

    private final Converter<Booking, BookingLinkedDto> bookingLinkedMapper;

    private final Converter<Comment, CommentDto> commentMapper;

    private final Long expectedItemId = 1L;

    private final Long expectedUserId = 2L;

    private final Long expectedCommentId = 3L;

    private final PageBuilder pageBuilder;

    private final ItemDto itemDto = new ItemDto(
            expectedItemId,
            "test_item_name",
            "test_description",
            Boolean.FALSE,
            null,
            null,
            null,
            Collections.emptyList()
    );

    private final User user = new User(
            "test_user_name",
            "test_email@test.domain.com"
    ).withId(expectedUserId);

    private final Item item = new Item(
            "test_item_name",
            "test_description",
            false,
            null,
            null
    ).withId(expectedItemId);

    private final Booking booking = new Booking(
            LocalDateTime.of(2043, 1, 1, 10, 0, 0),
            LocalDateTime.of(2043, 1, 1, 11, 0, 0),
            item.withId(expectedItemId),
            user.withId(expectedUserId),
            BookingStatus.APPROVED
    );

    private final Comment comment = new Comment(
            "test_text",
            user.withId(expectedUserId),
            item.withId(expectedItemId),
            LocalDateTime.now()
    ).withId(expectedCommentId);

    @Test
    @DisplayName("Create valid item test")
    void createValidItemTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.saveItem(any())).thenReturn(item);
        ItemDto result = itemService.createItem(expectedUserId, itemDto);
        assertThat(result.getId()).isEqualTo(expectedItemId);
        assertThat(result.getName()).isEqualTo(itemDto.getName());
        assertThat(result.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(result.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(result.getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(result.getLastBooking()).isEqualTo(itemDto.getLastBooking());
        assertThat(result.getNextBooking()).isEqualTo(itemDto.getNextBooking());
        assertThat(result.getComments()).asList().isEmpty();
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).saveItem(any());
    }

    @Test
    @DisplayName("Create item by not existing user test")
    void createItemByNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> itemService.createItem(expectedUserId, itemDto));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).saveItem(any());
    }

    @Test
    @DisplayName("Get existing item by not registered user test")
    void getExistingItemByNotRegisteredUserTest() throws Exception {
        when(itemRepository.findItemById(anyLong())).thenReturn(item);
        ItemDto result = itemService.getItem(null, expectedItemId);
        assertThat(result.getId()).isEqualTo(expectedItemId);
        assertThat(result.getName()).isEqualTo(itemDto.getName());
        assertThat(result.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(result.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(result.getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(result.getLastBooking()).isEqualTo(itemDto.getLastBooking());
        assertThat(result.getNextBooking()).isEqualTo(itemDto.getNextBooking());
        assertThat(result.getComments()).asList().isEmpty();
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, never()).findUserById(anyLong());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Get not existing item by not registered user test")
    void getNotExistingItemByNotRegisteredUserTest() throws Exception {
        when(itemRepository.findItemById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(ItemNotFoundException.class)
                .isThrownBy(() -> itemService.getItem(null, expectedItemId));
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, never()).findUserById(anyLong());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Get existing item by wrong user test")
    void getExistingItemByWrongUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        when(itemRepository.findItemById(anyLong())).thenReturn(item);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> itemService.getItem(expectedUserId, expectedItemId));
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Get existing item by owner test")
    void getExistingItemByOwnerTest() throws Exception {
        Booking lastBooking = booking
                .withStart(LocalDateTime.of(2043, 1, 1, 0, 0, 0))
                .withEnd(LocalDateTime.of(2043, 1, 1, 1, 0, 0));
        Booking nextBooking = booking
                .withStart(LocalDateTime.of(2043, 1, 1, 2, 0, 0))
                .withEnd(LocalDateTime.of(2043, 1, 1, 3, 0, 0));
        when(itemRepository.findItemById(anyLong())).thenReturn(item.withOwner(user));
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.getLastBookingByItemId(anyLong())).thenReturn(lastBooking);
        when(itemRepository.getNextBookingByItemId(anyLong())).thenReturn(nextBooking);
        when(itemRepository.findAllItemComments(anyLong())).thenReturn(Collections.emptyList());
        when(itemRepository.isUserCommentatorOfItem(anyLong(), anyLong())).thenReturn(Boolean.FALSE);
        ItemDto result = itemService.getItem(expectedUserId, expectedItemId);
        assertThat(result.getId()).isEqualTo(expectedItemId);
        assertThat(result.getName()).isEqualTo(itemDto.getName());
        assertThat(result.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(result.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(result.getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(result.getLastBooking()).isEqualTo(bookingLinkedMapper.convert(lastBooking));
        assertThat(result.getNextBooking()).isEqualTo(bookingLinkedMapper.convert(nextBooking));
        assertThat(result.getComments()).asList().isEmpty();
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findAllItemComments(anyLong());
        verify(itemRepository, times(1)).getLastBookingByItemId(anyLong());
        verify(itemRepository, times(1)).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Get existing item by commentator test")
    void getExistingItemByCommentatorTest() throws Exception {
        when(itemRepository.findItemById(anyLong())).thenReturn(item);
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findAllItemComments(anyLong())).thenReturn(Lists.list(comment));
        when(itemRepository.isUserCommentatorOfItem(anyLong(), anyLong())).thenReturn(Boolean.TRUE);
        ItemDto result = itemService.getItem(expectedUserId, expectedItemId);
        assertThat(result.getId()).isEqualTo(expectedItemId);
        assertThat(result.getName()).isEqualTo(itemDto.getName());
        assertThat(result.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(result.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(result.getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(result.getLastBooking()).isEqualTo(itemDto.getLastBooking());
        assertThat(result.getNextBooking()).isEqualTo(itemDto.getNextBooking());
        assertThat(result.getComments()).asList().isNotEmpty()
                .contains(commentMapper.convert(comment));
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Update existing item by owner test")
    void updateExistingItemByOwnerTest() throws Exception {
        String updatedDescription = "updated_description";
        String updatedName = "updated_name";
        Boolean updatedStatus = Boolean.TRUE;
        Booking lastBooking = booking
                .withStart(LocalDateTime.of(2043, 1, 1, 0, 0, 0))
                .withEnd(LocalDateTime.of(2043, 1, 1, 1, 0, 0));
        Booking nextBooking = booking
                .withStart(LocalDateTime.of(2043, 1, 1, 2, 0, 0))
                .withEnd(LocalDateTime.of(2043, 1, 1, 3, 0, 0));
        when(itemRepository.findItemById(anyLong()))
                .thenReturn(item.withId(expectedItemId).withOwner(user));
        when(itemRepository.updateItem(any()))
                .thenReturn(item
                        .withOwner(user)
                        .withDescription(updatedDescription)
                        .withName(updatedName)
                        .withAvailable(updatedStatus));
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.getLastBookingByItemId(anyLong())).thenReturn(lastBooking);
        when(itemRepository.getNextBookingByItemId(anyLong())).thenReturn(nextBooking);
        when(itemRepository.findAllItemComments(anyLong())).thenReturn(Lists.list(comment));
        when(itemRepository.isUserCommentatorOfItem(anyLong(), anyLong())).thenReturn(Boolean.FALSE);
        ItemDto result = itemService.updateItem(expectedUserId, expectedItemId,
                itemDto.withDescription(updatedDescription).withName(updatedName).withAvailable(updatedStatus));
        assertThat(result.getId()).isEqualTo(expectedItemId);
        assertThat(result.getName()).isEqualTo(updatedName);
        assertThat(result.getDescription()).isEqualTo(updatedDescription);
        assertThat(result.getAvailable()).isEqualTo(updatedStatus);
        assertThat(result.getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(result.getLastBooking()).isEqualTo(bookingLinkedMapper.convert(lastBooking));
        assertThat(result.getNextBooking()).isEqualTo(bookingLinkedMapper.convert(nextBooking));
        assertThat(result.getComments()).asList().isNotEmpty().contains(commentMapper.convert(comment));
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findAllItemComments(anyLong());
        verify(itemRepository, times(1)).getLastBookingByItemId(anyLong());
        verify(itemRepository, times(1)).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Update existing item by not existing test")
    void updateExistingItemByNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> itemService.updateItem(expectedUserId, expectedItemId, itemDto));
        verify(itemRepository, never()).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Update not existing item test")
    void updateNotExistingItemTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findItemById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(ItemNotFoundException.class)
                .isThrownBy(() -> itemService.updateItem(expectedUserId, expectedItemId, itemDto));
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Update existing item by wrong owner test")
    void updateExistingItemByWrongOwnerTest() throws Exception {
        Long wrongOwnerId = 99L;
        when(userRepository.findUserById(anyLong())).thenReturn(user.withId(wrongOwnerId));
        when(itemRepository.findItemById(anyLong())).thenReturn(item.withOwner(user));
        assertThatExceptionOfType(WrongItemOwnerException.class)
                .isThrownBy(() -> itemService.updateItem(wrongOwnerId, expectedItemId, itemDto));
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Delete existing item by owner test")
    void deleteExistingItemByOwnerTest() throws Exception {
        Booking lastBooking = booking
                .withStart(LocalDateTime.of(2043, 1, 1, 0, 0, 0))
                .withEnd(LocalDateTime.of(2043, 1, 1, 1, 0, 0));
        Booking nextBooking = booking
                .withStart(LocalDateTime.of(2043, 1, 1, 2, 0, 0))
                .withEnd(LocalDateTime.of(2043, 1, 1, 3, 0, 0));
        when(itemRepository.findItemById(anyLong())).thenReturn(item.withOwner(user));
        when(itemRepository.deleteItem(any())).thenReturn(item.withOwner(user));
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.getLastBookingByItemId(anyLong())).thenReturn(lastBooking);
        when(itemRepository.getNextBookingByItemId(anyLong())).thenReturn(nextBooking);
        when(itemRepository.findAllItemComments(anyLong())).thenReturn(Lists.list(comment));
        when(itemRepository.isUserCommentatorOfItem(anyLong(), anyLong())).thenReturn(Boolean.FALSE);
        ItemDto result = itemService.deleteItem(expectedUserId, expectedItemId);
        assertThat(result.getId()).isEqualTo(expectedItemId);
        assertThat(result.getName()).isEqualTo(itemDto.getName());
        assertThat(result.getDescription()).isEqualTo(itemDto.getDescription());
        assertThat(result.getAvailable()).isEqualTo(itemDto.getAvailable());
        assertThat(result.getRequestId()).isEqualTo(itemDto.getRequestId());
        assertThat(result.getLastBooking()).isEqualTo(bookingLinkedMapper.convert(lastBooking));
        assertThat(result.getNextBooking()).isEqualTo(bookingLinkedMapper.convert(nextBooking));
        assertThat(result.getComments()).asList().isNotEmpty().contains(commentMapper.convert(comment));
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findAllItemComments(anyLong());
        verify(itemRepository, times(1)).getLastBookingByItemId(anyLong());
        verify(itemRepository, times(1)).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Delete existing item by not existing test")
    void deleteExistingItemByNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> itemService.deleteItem(expectedUserId, expectedItemId));
        verify(itemRepository, never()).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Delete not existing item test")
    void deleteNotExistingItemTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findItemById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(ItemNotFoundException.class)
                .isThrownBy(() -> itemService.deleteItem(expectedUserId, expectedItemId));
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Delete existing item by wrong owner test")
    void deleteExistingItemByWrongOwnerTest() throws Exception {
        Long wrongOwnerId = 99L;
        when(userRepository.findUserById(anyLong())).thenReturn(user.withId(wrongOwnerId));
        when(itemRepository.findItemById(anyLong())).thenReturn(item.withOwner(user));
        assertThatExceptionOfType(WrongItemOwnerException.class)
                .isThrownBy(() -> itemService.deleteItem(wrongOwnerId, expectedItemId));
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Get all items by owner without page test")
    void getAllItemsByOwnerWithoutPageTest() throws Exception {
        Booking lastBooking = booking
                .withStart(LocalDateTime.of(2043, 1, 1, 0, 0, 0))
                .withEnd(LocalDateTime.of(2043, 1, 1, 1, 0, 0));
        Booking nextBooking = booking
                .withStart(LocalDateTime.of(2043, 1, 1, 2, 0, 0))
                .withEnd(LocalDateTime.of(2043, 1, 1, 3, 0, 0));
        when(itemRepository.findAllItems(anyLong()))
                .thenReturn(Lists.list(item
                        .withOwner(user)
                        .withAvailable(Boolean.TRUE)));
        when(itemRepository.getLastBookingByItemId(anyLong())).thenReturn(lastBooking);
        when(itemRepository.getNextBookingByItemId(anyLong())).thenReturn(nextBooking);
        when(itemRepository.findAllItemComments(anyLong())).thenReturn(Lists.list(comment));
        when(itemRepository.isUserCommentatorOfItem(anyLong(), anyLong())).thenReturn(Boolean.FALSE);
        Collection<ItemDto> result = itemService.getAllItems(expectedUserId, null, null);
        assertThat(result).asList().isNotEmpty().contains(
                itemDto
                        .withComments(Lists.list(commentMapper.convert(comment.withId(expectedCommentId))))
                        .withNextBooking(bookingLinkedMapper.convert(nextBooking))
                        .withLastBooking(bookingLinkedMapper.convert(lastBooking))
                        .withAvailable(Boolean.TRUE)
        );
        verify(itemRepository, never()).findItemById(anyLong());
        verify(userRepository, never()).findUserById(anyLong());
        verify(itemRepository, times(1)).findAllItems(anyLong());
        verify(itemRepository, times(1)).findAllItemComments(anyLong());
        verify(itemRepository, times(1)).getLastBookingByItemId(anyLong());
        verify(itemRepository, times(1)).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Get all items by owner with valid page test")
    void getAllItemsByOwnerWithValidPageTest() throws Exception {
        Integer from = 0;
        Integer size = 1;
        Booking lastBooking = booking
                .withStart(LocalDateTime.of(2043, 1, 1, 0, 0, 0))
                .withEnd(LocalDateTime.of(2043, 1, 1, 1, 0, 0));
        Booking nextBooking = booking
                .withStart(LocalDateTime.of(2043, 1, 1, 2, 0, 0))
                .withEnd(LocalDateTime.of(2043, 1, 1, 3, 0, 0));
        when(itemRepository.findAllItems(anyLong(), any()))
                .thenReturn(new PageImpl<>(Lists.list(item
                        .withOwner(user.withId(expectedUserId))
                        .withAvailable(Boolean.TRUE))));
        when(itemRepository.getLastBookingByItemId(anyLong())).thenReturn(lastBooking);
        when(itemRepository.getNextBookingByItemId(anyLong())).thenReturn(nextBooking);
        when(itemRepository.findAllItemComments(anyLong())).thenReturn(Lists.list(comment));
        when(itemRepository.isUserCommentatorOfItem(anyLong(), anyLong())).thenReturn(Boolean.FALSE);
        Collection<ItemDto> result = itemService.getAllItems(expectedUserId, from, size);
        assertThat(result).asList().isNotEmpty().contains(
                itemDto
                        .withComments(Lists.list(commentMapper.convert(comment.withId(expectedCommentId))))
                        .withNextBooking(bookingLinkedMapper.convert(nextBooking))
                        .withLastBooking(bookingLinkedMapper.convert(lastBooking))
                        .withAvailable(Boolean.TRUE)
        );
        verify(itemRepository, never()).findItemById(anyLong());
        verify(userRepository, never()).findUserById(anyLong());
        verify(itemRepository, times(1)).findAllItems(anyLong(), any());
        verify(itemRepository, times(1)).findAllItemComments(anyLong());
        verify(itemRepository, times(1)).getLastBookingByItemId(anyLong());
        verify(itemRepository, times(1)).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Get all items by owner with wrong page test")
    void getAllItemsByOwnerWithWrongPageTest() throws Exception {
        Integer from = -1;
        Integer size = 0;
        assertThatExceptionOfType(InvalidPaginationParamsException.class)
                .isThrownBy(() -> itemService.getAllItems(expectedUserId, from, size));
        verify(itemRepository, never()).findItemById(anyLong());
        verify(userRepository, never()).findUserById(anyLong());
        verify(itemRepository, never()).findAllItems(anyLong(), any());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Search items without page test")
    void searchItemsWithoutPageTest() throws Exception {
        String text = "test";
        when(itemRepository.searchItem(anyString()))
                .thenReturn(Lists.list(item
                        .withOwner(user.withId(expectedUserId))
                        .withAvailable(Boolean.TRUE)));
        Collection<ItemDto> result = itemService.searchItem(text, null, null);
        assertThat(result).asList().isNotEmpty().contains(itemDto.withAvailable(Boolean.TRUE));
        verify(itemRepository, never()).findItemById(anyLong());
        verify(userRepository, never()).findUserById(anyLong());
        verify(itemRepository, times(1)).searchItem(anyString());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Search items with valid page test")
    void searchItemsWithValidPageTest() throws Exception {
        Integer from = 0;
        Integer size = 1;
        String text = "test";
        when(itemRepository.searchItem(anyString(), any()))
                .thenReturn(new PageImpl<>(Lists.list(item
                        .withOwner(user)
                        .withAvailable(Boolean.TRUE))));
        when(itemRepository.isUserCommentatorOfItem(anyLong(), anyLong())).thenReturn(Boolean.FALSE);
        Collection<ItemDto> result = itemService.searchItem(text, from, size);
        assertThat(result).asList().isNotEmpty().contains(itemDto.withAvailable(Boolean.TRUE));
        verify(itemRepository, never()).findItemById(anyLong());
        verify(userRepository, never()).findUserById(anyLong());
        verify(itemRepository, times(1)).searchItem(anyString(), any());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Search items with wrong page test")
    void searchItemsWithWrongPageTest() throws Exception {
        Integer from = -1;
        Integer size = 0;
        String text = "test";
        assertThatExceptionOfType(InvalidPaginationParamsException.class)
                .isThrownBy(() -> itemService.searchItem(text, from, size));
        verify(itemRepository, never()).findItemById(anyLong());
        verify(userRepository, never()).findUserById(anyLong());
        verify(itemRepository, never()).searchItem(anyString());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Search items with empty text test")
    void searchItemsWithEmptyTextTest() throws Exception {
        assertThat(itemService.searchItem("", null, null)).asList().isEmpty();
        verify(itemRepository, never()).findItemById(anyLong());
        verify(userRepository, never()).findUserById(anyLong());
        verify(itemRepository, never()).searchItem(anyString());
        verify(itemRepository, never()).findAllItemComments(anyLong());
        verify(itemRepository, never()).getLastBookingByItemId(anyLong());
        verify(itemRepository, never()).getNextBookingByItemId(anyLong());
    }

    @Test
    @DisplayName("Create valid comment test")
    void createValidCommentTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findItemById(anyLong())).thenReturn(item);
        when(itemRepository.createComment(any())).thenReturn(comment.withAuthor(user));
        when(itemRepository.isUserRealBookerOfItem(anyLong(), anyLong())).thenReturn(Boolean.TRUE);
        CommentDto result = itemService.createComment(expectedUserId, expectedItemId, commentMapper.convert(comment));
        assertThat(result).isEqualTo(commentMapper.convert(comment)
                .withId(expectedCommentId)
                .withAuthorName(user.getName()));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(itemRepository, times(1)).createComment(any());
    }

    @Test
    @DisplayName("Create comment by not existing user test")
    void createCommentByNotExistingUserTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(UserNotFoundException.class)
                .isThrownBy(() -> itemService.createComment(
                        expectedUserId, expectedItemId, commentMapper.convert(comment)));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, never()).findItemById(anyLong());
        verify(itemRepository, never()).createComment(any());
    }

    @Test
    @DisplayName("Create comment for not existing item test")
    void createCommentForNotExistingItemTest() throws Exception {
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findItemById(anyLong())).thenReturn(null);
        assertThatExceptionOfType(ItemNotFoundException.class)
                .isThrownBy(() -> itemService.createComment(
                        expectedUserId, expectedItemId, commentMapper.convert(comment)));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(itemRepository, never()).createComment(any());
    }

    @Test
    @DisplayName("Create comment by not booker test")
    void createCommentByNotBookerTest() throws Exception {
        Long wrongBookerId = 99L;
        when(userRepository.findUserById(anyLong())).thenReturn(user);
        when(itemRepository.findItemById(anyLong())).thenReturn(item.withOwner(user));
        assertThatExceptionOfType(CommentCreateException.class)
                .isThrownBy(() -> itemService.createComment(
                        wrongBookerId, expectedItemId, commentMapper.convert(comment)));
        verify(userRepository, times(1)).findUserById(anyLong());
        verify(itemRepository, times(1)).findItemById(anyLong());
        verify(itemRepository, never()).createComment(any());
    }

    @Test
    @DisplayName("Page builder valid page test")
    void PageBuilderValidPageTest() throws Exception {
        Integer from = 0;
        Integer size = 1;
        assertThat(pageBuilder.build(from, size, null)).isNotNull();
    }

    @Test
    @DisplayName("Page builder invalid page test")
    void PageBuilderInvalidPageTest() throws Exception {
        Integer from = -1;
        Integer size = 0;
        assertThatExceptionOfType(InvalidPaginationParamsException.class)
                .isThrownBy(() -> pageBuilder.build(from, size, null));
    }
}
