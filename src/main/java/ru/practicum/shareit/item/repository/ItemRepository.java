package ru.practicum.shareit.item.repository;

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

    Collection<Item> searchItem(String text);

    Booking getNextBookingByItemId(Long itemId);

    Booking getLastBookingByItemId(Long itemId);

    Boolean isUserRealBookerOfItem(Long itemId, Long userId);

    Boolean isUserCommentatorOfItem(Long itemId, Long userId);

    Comment createComment(Comment comment);

    Collection<Comment> findAllItemComments(Long itemId);
}
