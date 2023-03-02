package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.booking.Booking;
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
}
