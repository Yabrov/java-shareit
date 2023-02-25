package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {

    ItemDto getItem(Long itemId);

    ItemDto createItem(Long userId, ItemDto itemDto);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);

    ItemDto deleteItem(Long userId, Long itemId);

    Collection<ItemDto> getAllItems(Long userId);

    Collection<ItemDto> searchItem(String text);
}
