package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {

    Item findItemById(Long itemId);

    Item deleteItem(Long itemId);

    Item saveItem(Item item);

    Item updateItem(Item item);

    Collection<Item> findAllItems(Long userId);

    Page<Item> findAllItems(Long userId, Pageable pageable);

    Collection<Item> searchItem(String text);

    Page<Item> searchItem(String text, Pageable pageable);

    Booking getNextBookingByItemId(Long itemId);

    Booking getLastBookingByItemId(Long itemId);

    Boolean isUserRealBookerOfItem(Long itemId, Long userId);

    Boolean isUserCommentatorOfItem(Long itemId, Long userId);

    Comment createComment(Comment comment);

    Collection<Comment> findAllItemComments(Long itemId);
}
